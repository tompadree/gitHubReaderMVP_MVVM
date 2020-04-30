package com.githubreader.gitresults

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.githubreader.data.models.RepoObject
import com.githubreader.data.source.FakeRepository
import com.githubreader.di.AppModule
import com.githubreader.di.DataModule
import com.githubreader.di.NetModule
import com.githubreader.getOrAwaitValue
import com.githubreader.observeForTesting
import com.githubreader.util.MainCoroutineRule
import com.githubreader.utils.network.InternetConnectionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import com.google.common.truth.Truth.*

/**
 * @author Tomislav Curis
 */

@ExperimentalCoroutinesApi
class GitResultsViewModelTest : KoinTest {


    // What is testing
    private lateinit var gitResultsViewModel: GitResultsViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var repository: FakeRepository

    // Rule for koin injection
    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(listOf(AppModule, DataModule, NetModule))
    }

    private val internetConnectionManager: InternetConnectionManager by inject()

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        repository = FakeRepository()
        val repoObject = RepoObject(1,"Repo1")
        val repoObject2 = RepoObject(2,"Repo2")
        val repoObject3 = RepoObject(3,"Repo3")
        repository.currentListRepos = mutableListOf(repoObject, repoObject2, repoObject3)

        gitResultsViewModel = GitResultsViewModel(repository, internetConnectionManager)
    }

    @Test
    fun loadAllReposToView() {
        // Pause dispatcher so we can verify initial values
        mainCoroutineRule.pauseDispatcher()

        // Trigger loading of repos
        gitResultsViewModel.refresh(true)

        // Observe the items to keep LiveData emitting
        gitResultsViewModel.items.observeForTesting {

            // Then progress indicator is shown
            assertThat(gitResultsViewModel.dataLoading.getOrAwaitValue()).isTrue()

            // Execute pending coroutines actions
            mainCoroutineRule.resumeDispatcher()

            // Then progress indicator is hidden
            assertThat(gitResultsViewModel.dataLoading.getOrAwaitValue()).isFalse()

            // And data correctly loaded
            assertThat(gitResultsViewModel.items.getOrAwaitValue()).hasSize(3)
        }
    }

    @Test
    fun fetchingReposGetError() {
        // Pause dispatcher so we can verify initial values
        mainCoroutineRule.pauseDispatcher()

        // Set repo return error
        repository.setReturnError(true)

        // StartFetching
        gitResultsViewModel.refresh(true)

        // Observe the items to keep LiveData emitting
        gitResultsViewModel.items.observeForTesting {

            // Loding
            assertThat(gitResultsViewModel.dataLoading.getOrAwaitValue()).isTrue()

            // Execute pending coroutines actions
            mainCoroutineRule.resumeDispatcher()

            // loading is done
            assertThat(gitResultsViewModel.dataLoading.getOrAwaitValue()).isFalse()

            // If isDataLoadingError response was error
            assertThat(gitResultsViewModel.isDataLoadingError.value).isEqualTo(true)
        }
    }
}