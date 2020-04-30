package com.githubreader.data.source

import androidx.lifecycle.LiveData
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject
import com.githubreader.data.models.Result
import com.githubreader.data.source.local.GitHubResultsLocalDataSource
import com.githubreader.data.source.remote.GitHubResultsRemoteDataSource
import com.githubreader.util.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception
import com.google.common.truth.Truth.*
import junit.framework.Assert.assertEquals
import org.hamcrest.core.IsEqual

/**
 * @author Tomislav Curis
 */

@ExperimentalCoroutinesApi
class GitHubResultsRepositoryTest {

    // Dataset
    private val ownerObject = OwnerObject("User1","")
    private val ownerObject2 = OwnerObject("User2","Repo2")
    private val ownerObject3 = OwnerObject("User3","Repo3")
    private val remoteSubscribers = listOf(ownerObject, ownerObject3).sortedBy { it.userName }
    private val localSubscribers = listOf(ownerObject2, ownerObject3).sortedBy { it.userName }
    private val newSubscribers = listOf(ownerObject, ownerObject2).sortedBy { it.userName }


    private val repoObject = RepoObject(1,"Repo1")
    private val repoObject2 = RepoObject(2,"Repo2")
    private val repoObject3 = RepoObject(3,"Repo3")
    private val remoteRepos = listOf(repoObject, repoObject3).sortedBy { it.repoId }
    private val localRepos = listOf(repoObject2, repoObject3).sortedBy { it.repoId }
    private val newRepos = listOf(repoObject).sortedBy { it.repoId }


    private lateinit var gitHubResultsRemoteDataSource: FakeDataSource
    private lateinit var gitHubResultsLocalDataSource: FakeDataSource

    private lateinit var repository: GitHubResultsRepositoryImpl

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun createRepository() {
        gitHubResultsLocalDataSource = FakeDataSource(localRepos.toMutableList(), localSubscribers.toMutableList())
        gitHubResultsRemoteDataSource = FakeDataSource(remoteRepos.toMutableList(), remoteSubscribers.toMutableList())

        repository = GitHubResultsRepositoryImpl(gitHubResultsLocalDataSource, gitHubResultsRemoteDataSource)
    }

    @Test
    fun getResults_emptyRepositoryAndUninitializedCache() = mainCoroutineRule.runBlockingTest {
        val emptySource = FakeDataSource()
        val tempRepository = GitHubResultsRepositoryImpl(emptySource, emptySource)

        assertThat(tempRepository.getGitHubResults(true, "Repo1", 1, 1) is Result.Success).isTrue()
        assertThat(tempRepository.getGitHubResultSubscribers(true, "Repo1", 1, 1) is Result.Success).isTrue()
    }

    @Test
    fun getRepos_repoCacheAfterFirstApiCall () = mainCoroutineRule.runBlockingTest {
        // false trigger is default
        val initial = repository.getGitHubResults(false, "Repo", 1, 30)

        gitHubResultsRemoteDataSource.repos = newRepos.toMutableList()

        val second = repository.getGitHubResults(false, "Repo", 1, 30)

        // Initial and second should match because no backend is called
        assertThat(second).isEqualTo(initial)
    }

    @Test
    fun getOwners_repoCacheAfterFirstApiCall () = mainCoroutineRule.runBlockingTest {
        // false trigger is default
        val initial = repository.getGitHubResultSubscribers(false, "Repo", 1, 30)

        gitHubResultsRemoteDataSource.owners = newSubscribers.toMutableList()

        val second = repository.getGitHubResultSubscribers(false, "Repo", 1, 30)

        // Initial and second should match because no backend is called
        assertThat(second).isEqualTo(initial)
    }

    @Test
    fun getRepos_requestsAllReposFromRemoteDataSource() = mainCoroutineRule.runBlockingTest {
        // When repos are requested from the repos repository
        val repos = repository.getGitHubResults(true, "Repo", 1, 30) as Result.Success

        // Then repos are loaded from the remote data source
        assertThat(repos.data).isEqualTo(remoteRepos)
    }

    @Test
    fun getRepos_requestsAllReposFromLocalDataSource() = mainCoroutineRule.runBlockingTest {
        // When repos are requested from the repos repository
        val repos = repository.getGitHubResults(false, "Repo", 1, 30) as Result.Success

        // Then repos are loaded from the local data source
        assertThat(repos.data).isEqualTo(localRepos)
    }

    @Test
    fun getOwners_requestsAllReposFromRemoteDataSource() = mainCoroutineRule.runBlockingTest {
        // When repos are requested from the repos repository
        val repos = repository.getGitHubResultSubscribers(true, "Repo", 1, 30) as Result.Success

        // Then repos are loaded from the remote data source
        assertThat(repos.data).isEqualTo(remoteSubscribers)
    }

    @Test
    fun getOwners_requestsAllReposFromLocalDataSource() = mainCoroutineRule.runBlockingTest {
        // When repos are requested from the repos repository
        val repos = repository.getGitHubResultSubscribers(false, "Repo", 1, 30) as Result.Success

        // Then repos are loaded from the local data source
        assertThat(repos.data).isEqualTo(localSubscribers)
    }

    @Test
    fun saveRepos_savesToLocal() = mainCoroutineRule.runBlockingTest {
        // When rates are requested from the rates repository
        val repos = repository.getGitHubResults(true, "Repo", 1, 30) as Result.Success

        // Save rates
        repository.saveGitHubResultsDB("Repo", repos.data)

        // Fetch them
        val reposLocal = repository.getGitHubResults(true, "User", 1, 30) as Result.Success

        assertThat(repos.data).isEqualTo(reposLocal.data)
    }

    @Test
    fun saveOwners_savesToLocal() = mainCoroutineRule.runBlockingTest {
        // When rates are requested from the rates repository
        val owners = repository.getGitHubResultSubscribers(true, "Repo", 1, 30) as Result.Success

        // Save rates
        repository.saveGitHubResultSubscribersDB("Repo", owners.data)

        // Fetch them
        val ownersLocal = repository.getGitHubResultSubscribers(true, "Repo", 1, 30) as Result.Success

        assertThat(owners.data).isEqualTo(ownersLocal.data)
    }

    @Test
    fun getRepos_WithDirtyCache_reposAreRetrievedFromRemote() = mainCoroutineRule.runBlockingTest {
        // First call returns from REMOTE
        val repos = repository.getGitHubResults(false, "Repo", 1, 30) as Result.Success

        // Set a different list of repos in REMOTE
        gitHubResultsRemoteDataSource.repos = newRepos.toMutableList()

        // But if repos are cached, subsequent calls load from cache
        val cachedrepos = repository.getGitHubResults(false, "Repo", 1, 30) as Result.Success
        assertThat(cachedrepos).isEqualTo(repos)

        // Now force remote loading
        val refreshedRepos = repository.getGitHubResults(true, "Repo", 1, 30) as Result.Success

        // repos must be the recently updated in REMOTE
        assertThat(refreshedRepos.data).isEqualTo(newRepos)
    }

    @Test
    fun getRepos_remoteUnavailable_error() = mainCoroutineRule.runBlockingTest {
            // Make remote data source unavailable
            gitHubResultsRemoteDataSource.repos = null

            // Load repos forcing remote load
            val repos = repository.getGitHubResults(true, "Repo", 1, 30)

            // Result should be an error
            assertThat(repos).isInstanceOf(Result.Error::class.java)
        }

    @Test
    fun getRepos_WithRemoteDataSourceUnavailable_reposAreRetrievedFromLocal() =
        mainCoroutineRule.runBlockingTest {
            // When the remote data source is unavailable
            gitHubResultsRemoteDataSource.repos = null

            // The repository fetches from the local source
            assertThat((repository.getGitHubResults(false, "Repo", 1, 30) as Result.Success).data).isEqualTo(localRepos)
        }

    @Test
    fun getRepos_WithBothDataSourcesUnavailable_returnsError() = mainCoroutineRule.runBlockingTest {
        // When both sources are unavailable
        gitHubResultsRemoteDataSource.repos = null
        gitHubResultsLocalDataSource.repos = null

        // The repository returns an error
        assertThat(repository.getGitHubResults(false, "Repo", 1, 30)).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun getOwners_remoteUnavailable_error() = mainCoroutineRule.runBlockingTest {
        // Make remote data source unavailable
        gitHubResultsRemoteDataSource.owners = null

        // Load repos forcing remote load
        val repos = repository.getGitHubResultSubscribers(true, "Repo", 1, 30)

        // Result should be an error
        assertThat(repos).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun getOwners_WithRemoteDataSourceUnavailable_reposAreRetrievedFromLocal() =
        mainCoroutineRule.runBlockingTest {
            // When the remote data source is unavailable
            gitHubResultsRemoteDataSource.owners = null

            // The repository fetches from the local source
            assertThat((repository.getGitHubResultSubscribers(false, "Repo", 1, 30) as Result.Success).data)
                .isEqualTo(localSubscribers)
        }

    @Test
    fun getOwners_WithBothDataSourcesUnavailable_returnsError() = mainCoroutineRule.runBlockingTest {
        // When both sources are unavailable
        gitHubResultsRemoteDataSource.owners = null
        gitHubResultsLocalDataSource.owners = null

        // The repository returns an error
        assertThat(repository.getGitHubResultSubscribers(false, "Repo", 1, 30)).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun getRepos_refreshRatesFromRemoteDataSource() = mainCoroutineRule.runBlockingTest {
        // Initial state in db
        val initial = gitHubResultsLocalDataSource.repos

        // Fetch from remote
        val remoteReposTemp = repository.getGitHubResults(true, "Repo", 1, 30) as Result.Success

        assertEquals(remoteReposTemp.data, remoteRepos)
        assertEquals(remoteReposTemp.data, gitHubResultsLocalDataSource.repos)
        assertThat(gitHubResultsLocalDataSource.repos).isEqualTo(initial)
    }

    @Test
    fun getOwners_refreshRatesFromRemoteDataSource() = mainCoroutineRule.runBlockingTest {
        // Initial state in db
        val initial = gitHubResultsLocalDataSource.owners

        // Fetch from remote
        val remoteOwnersTemp = repository.getGitHubResultSubscribers(true, "Repo", 1, 30) as Result.Success

        assertEquals(remoteOwnersTemp.data, remoteSubscribers)
        assertEquals(remoteOwnersTemp.data, gitHubResultsLocalDataSource.owners)
        assertThat(gitHubResultsLocalDataSource.owners).isEqualTo(initial)
    }

    @Test
    fun getRepos_deleteRepos() = mainCoroutineRule.runBlockingTest {
        // Get rates
        val initialRepos = repository.getGitHubResults(false, "Repo", 1, 30) as? Result.Success

        gitHubResultsLocalDataSource.deleteRepos()

        // Fetch after delete
        val afterDeleteRepos = repository.getGitHubResults(false, "Repo", 1, 30) as? Result.Success

        //check
        assertThat(initialRepos?.data).isNotEmpty()
        assertThat(afterDeleteRepos?.data).isEmpty()
    }

    @Test
    fun getOwners_deleteOwners() = mainCoroutineRule.runBlockingTest {
        // Get rates
        val initialOwners = repository.getGitHubResultSubscribers(false, "Repo", 1, 30) as? Result.Success

        gitHubResultsLocalDataSource.deleteRepos()

        // Fetch after delete
        val afterDeleteOwners = repository.getGitHubResultSubscribers(false, "Repo", 1, 30) as? Result.Success

        //check
        assertThat(initialOwners?.data).isNotEmpty()
        assertThat(afterDeleteOwners?.data).isNotEmpty()
    }
}