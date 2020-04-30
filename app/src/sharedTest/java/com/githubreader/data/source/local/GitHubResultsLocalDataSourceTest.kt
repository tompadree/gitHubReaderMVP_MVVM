package com.githubreader.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject
import com.githubreader.data.models.Result
import com.githubreader.util.MainCoroutineRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.MatcherAssert.assertThat
import com.google.common.truth.Truth.assertThat
import org.hamcrest.CoreMatchers.`is`

/**
 * @author Tomislav Curis
 *
 * Integration test for the [CurrenciesDataSource].
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class GitHubResultsLocalDataSourceTest {

    private lateinit var database: GitHubDatabase
    private lateinit var localDataSource: GitHubResultsLocalDataSource

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDB () {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), GitHubDatabase::class.java).build()
        localDataSource = GitHubResultsLocalDataSource(database.getGitHubDao(), Dispatchers.Main) // It has to be on Main
    }

    @After
    fun closeDB() = database.close()

    private var owners = listOf(OwnerObject("User1","Repo1"), OwnerObject("User2","Repo2"))
    private var repos = listOf(RepoObject(1,"Repo1"), RepoObject(2,"Repo2"))

    @Test
    fun insertReposAndGet() = mainCoroutineRule.runBlockingTest {
        // Insert a repo
        localDataSource.saveGitHubResultsDB(repos)

        // retrieve list
        val reposTemp = localDataSource.getGitHubResults("%%",0,5) as Result.Success

        assertThat(reposTemp.data[0].repoId).isEqualTo(repos[0].repoId)
        assertThat(reposTemp.data[0].repoName).isEqualTo(repos[0].repoName)
        assertThat(reposTemp.data[1].repoId).isEqualTo(repos[1].repoId)
        assertThat(reposTemp.data[1].repoName).isEqualTo(repos[1].repoName)
    }

    @Test
    fun insertReposAndGetByRepoName() = runBlockingTest {
        // Insert repos
        localDataSource.saveGitHubResultsDB(repos)

        // retrieve list
        val reposTemp = localDataSource.getGitHubResults("%Repo%",0,5) as Result.Success

        assertThat(reposTemp.data[0].repoId).isEqualTo(repos[0].repoId)
        assertThat(reposTemp.data[0].repoName).isEqualTo(repos[0].repoName)
        assertThat(reposTemp.data[1].repoId).isEqualTo(repos[1].repoId)
        assertThat(reposTemp.data[1].repoName).isEqualTo(repos[1].repoName)
    }

    @Test
    fun insertOwnersAndGet() = runBlockingTest {
        // Insert subscribers
        localDataSource.saveGitHubResultSubscribersDB("Repo1", owners)

        // retrieve list
        val ownersTemp = localDataSource.getGitHubResultSubscribers("%%",0,5) as Result.Success

        assertThat(ownersTemp.data[0].userName).isEqualTo(owners[0].userName)
        assertThat(ownersTemp.data[0].parentRepo).isEqualTo(owners[0].parentRepo)
        assertThat(ownersTemp.data[1].userName).isEqualTo(owners[1].userName)
        assertThat(ownersTemp.data[1].parentRepo).isEqualTo(owners[1].parentRepo)
    }

    @Test
    fun insertOwnersAndGetByOwnersName() = runBlockingTest {
        // Insert subscribers
        localDataSource.saveGitHubResultSubscribersDB("Repo1", owners)

        // retrieve list
        val ownersTemp = localDataSource.getGitHubResultSubscribers("%Repo%",0,5) as Result.Success

        assertThat(ownersTemp.data[0].userName).isEqualTo(owners[0].userName)
        assertThat(ownersTemp.data[0].parentRepo).isEqualTo(owners[0].parentRepo)
        assertThat(ownersTemp.data[1].userName).isEqualTo(owners[1].userName)
        assertThat(ownersTemp.data[1].parentRepo).isEqualTo(owners[1].parentRepo)
    }

    @Test
    fun insertReposReplacesOnConflict() = runBlockingTest {
        // Insert repos
        localDataSource.saveGitHubResultsDB(repos)

        // When a task with the same id is inserted
        val newRepos = listOf(RepoObject(1,"Repo1"), RepoObject(2,"Repo2"))
        localDataSource.saveGitHubResultsDB(newRepos)

        // retrieve list
        val reposTemp = localDataSource.getGitHubResults("%Repo%",0,5) as Result.Success


        assertThat(reposTemp.data[0].repoId, `is` (repos[0].repoId))
        assertThat(reposTemp.data[0].repoName, `is` (repos[0].repoName))
        assertThat(reposTemp.data[1].repoId, `is` (repos[1].repoId))
        assertThat(reposTemp.data[1].repoName, `is` (repos[1].repoName))
    }

    @Test
    fun insertOwnersReplacesOnConflict() = runBlockingTest {
        // Insert subscribers
        localDataSource.saveGitHubResultSubscribersDB("Repo1", owners)

        // When a task with the same id is inserted
        val newSubscribers = listOf(OwnerObject("User1","Repo1"), OwnerObject("User2","Repo2"))
        localDataSource.saveGitHubResultSubscribersDB("Repo1", newSubscribers)

        // retrieve list
        val ownersTemp = localDataSource.getGitHubResultSubscribers("%Repo%",0,5) as Result.Success


        assertThat(ownersTemp.data[0].userName, `is` (owners[0].userName))
        assertThat(ownersTemp.data[0].parentRepo, `is` (owners[0].parentRepo))
        assertThat(ownersTemp.data[1].userName, `is` (owners[1].userName))
        assertThat(ownersTemp.data[1].parentRepo, `is` (owners[1].parentRepo))
    }
}