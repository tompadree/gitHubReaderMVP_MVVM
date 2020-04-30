package com.githubreader.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import com.google.common.truth.Truth.assertThat
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Tomislav Curis
 */

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class GitHubDAOTest{

    private lateinit var database: GitHubDatabase
    private lateinit var currenciesDAO: GitHubDAO

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDB () {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), GitHubDatabase::class.java).build()
        currenciesDAO = database.getGitHubDao()
    }

    @After
    fun closeDB() = database.close()

    private var owners = listOf(OwnerObject("User1","Repo1"), OwnerObject("User2","Repo2"))
    private var repos = listOf(RepoObject(1,"Repo1"), RepoObject(2,"Repo2"))

    @Test
    fun insertReposAndGet() = runBlockingTest {
        // Insert a repo
        currenciesDAO.saveGitHubResults(repos)

        // retrieve list
        val reposTemp = currenciesDAO.getGitHubResults("%%",0,5)

        assertThat(reposTemp[0].repoId).isEqualTo(repos[0].repoId)
        assertThat(reposTemp[0].repoName).isEqualTo(repos[0].repoName)
        assertThat(reposTemp[1].repoId).isEqualTo(repos[1].repoId)
        assertThat(reposTemp[1].repoName).isEqualTo(repos[1].repoName)
    }

    @Test
    fun insertReposAndGetByRepoName() = runBlockingTest {
        // Insert repos
        currenciesDAO.saveGitHubResults(repos)

        // retrieve list
        val reposTemp = currenciesDAO.getGitHubResults("%Repo%",0,5)

        assertThat(reposTemp[0].repoId).isEqualTo(repos[0].repoId)
        assertThat(reposTemp[0].repoName).isEqualTo(repos[0].repoName)
        assertThat(reposTemp[1].repoId).isEqualTo(repos[1].repoId)
        assertThat(reposTemp[1].repoName).isEqualTo(repos[1].repoName)
    }

    @Test
    fun insertOwnersAndGet() = runBlockingTest {
        // Insert subscribers
        currenciesDAO.saveGitHubResultSubscribers(owners)

        // retrieve list
        val ownersTemp = currenciesDAO.getGitHubResultSubscribers("%%",0,5)

        assertThat(ownersTemp[0].userName).isEqualTo(owners[0].userName)
        assertThat(ownersTemp[0].parentRepo).isEqualTo(owners[0].parentRepo)
        assertThat(ownersTemp[1].userName).isEqualTo(owners[1].userName)
        assertThat(ownersTemp[1].parentRepo).isEqualTo(owners[1].parentRepo)
    }

    @Test
    fun insertOwnersAndGetByOwnersName() = runBlockingTest {
        // Insert subscribers
        currenciesDAO.saveGitHubResultSubscribers(owners)

        // retrieve list
        val ownersTemp = currenciesDAO.getGitHubResultSubscribers("%Repo%",0,5)

        assertThat(ownersTemp[0].userName).isEqualTo(owners[0].userName)
        assertThat(ownersTemp[0].parentRepo).isEqualTo(owners[0].parentRepo)
        assertThat(ownersTemp[1].userName).isEqualTo(owners[1].userName)
        assertThat(ownersTemp[1].parentRepo).isEqualTo(owners[1].parentRepo)
    }

    @Test
    fun insertReposReplacesOnConflict() = runBlockingTest {
        // Insert repos
        currenciesDAO.saveGitHubResults(repos)

        // When a task with the same id is inserted
        val newRepos = listOf(RepoObject(1,"Repo1"), RepoObject(2,"Repo2"))
        currenciesDAO.saveGitHubResults(newRepos)

        // retrieve list
        val reposTemp = currenciesDAO.getGitHubResults("%Repo%",0,5)


        assertThat(reposTemp[0].repoId, `is` (repos[0].repoId))
        assertThat(reposTemp[0].repoName, `is` (repos[0].repoName))
        assertThat(reposTemp[1].repoId, `is` (repos[1].repoId))
        assertThat(reposTemp[1].repoName, `is` (repos[1].repoName))
    }

    @Test
    fun insertOwnersReplacesOnConflict() = runBlockingTest {
        // Insert subscribers
        currenciesDAO.saveGitHubResultSubscribers(owners)

        // When a task with the same id is inserted
        val newSubscribers = listOf(OwnerObject("User1","Repo1"), OwnerObject("User2","Repo2"))
        currenciesDAO.saveGitHubResultSubscribers(newSubscribers)

        // retrieve list
        val ownersTemp = currenciesDAO.getGitHubResultSubscribers("%Repo%",0,5)


        assertThat(ownersTemp[0].userName, `is` (owners[0].userName))
        assertThat(ownersTemp[0].parentRepo, `is` (owners[0].parentRepo))
        assertThat(ownersTemp[1].userName, `is` (owners[1].userName))
        assertThat(ownersTemp[1].parentRepo, `is` (owners[1].parentRepo))
    }

    @Test
    fun deleteRepos() = runBlockingTest {
        // Insert repos
        currenciesDAO.saveGitHubResults(repos)

        // Delete ratesObject
        currenciesDAO.deleteRepos()

        // retrieve object
        val reposTemp = currenciesDAO.getGitHubResults("%Repo%",0,5)

        assertThat(reposTemp.isEmpty(), `is`(true))
    }

    @Test
    fun deleteSubscribers() = runBlockingTest {
        // Insert subscribers
        currenciesDAO.saveGitHubResultSubscribers(owners)

        // Delete ratesObject
        currenciesDAO.deleteOwners()

        // retrieve object
        val ownersTemp = currenciesDAO.getGitHubResultSubscribers("%Repo%",0,5)

        assertThat(ownersTemp.isEmpty(), `is`(true))
    }

}