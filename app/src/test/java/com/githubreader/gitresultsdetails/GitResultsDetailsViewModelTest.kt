package com.githubreader.gitresultsdetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.source.FakeRepository
import com.githubreader.di.AppModule
import com.githubreader.di.DataModule
import com.githubreader.di.NetModule
import com.githubreader.getOrAwaitValue
import com.githubreader.observeForTesting
import com.githubreader.util.MainCoroutineRule
import com.githubreader.utils.network.InternetConnectionManager
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

/**
 * @author Tomislav Curis
 */

@ExperimentalCoroutinesApi
class GitResultsDetailsViewModelTest : KoinTest {

    // What is testing
    private lateinit var gitResultsDetailsViewModel: GitResultsDetailsViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var repository: FakeRepository

    // Rule for koin injection
    @get:Rule
    val koinTestRule = KoinTestRule.create {
//        MockitoAnnotations.initMocks(Context::class.java)
//        androidContext(context)
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
        val ownerObject = OwnerObject("User1","")
        val ownerObject2 = OwnerObject("User2","Repo2")
        val ownerObject3 = OwnerObject("User3","Repo3")
        repository.currentListOwners = mutableListOf(ownerObject, ownerObject2, ownerObject3)

        gitResultsDetailsViewModel = GitResultsDetailsViewModel(repository, internetConnectionManager)
    }

    @Test
    fun loadAllSubscribersToView() {
        // Pause dispatcher so we can verify initial values
        mainCoroutineRule.pauseDispatcher()

        // Trigger loading of repos
        gitResultsDetailsViewModel.refresh(true)

        // Observe the items to keep LiveData emitting
        gitResultsDetailsViewModel.items.observeForTesting {

            // Then progress indicator is shown
            Truth.assertThat(gitResultsDetailsViewModel.dataLoading.getOrAwaitValue()).isTrue()

            // Execute pending coroutines actions
            mainCoroutineRule.resumeDispatcher()

            // Then progress indicator is hidden
            Truth.assertThat(gitResultsDetailsViewModel.dataLoading.getOrAwaitValue()).isFalse()

            // And data correctly loaded
            Truth.assertThat(gitResultsDetailsViewModel.items.getOrAwaitValue()).hasSize(3)
        }
    }

    @Test
    fun fetchingSubscribersGetError() {
        // Pause dispatcher so we can verify initial values
        mainCoroutineRule.pauseDispatcher()

        // Set repo return error
        repository.setReturnError(true)

        // StartFetching
        gitResultsDetailsViewModel.refresh(true)

        // Observe the items to keep LiveData emitting
        gitResultsDetailsViewModel.items.observeForTesting {

            // Loding
            Truth.assertThat(gitResultsDetailsViewModel.dataLoading.getOrAwaitValue()).isTrue()

            // Execute pending coroutines actions
            mainCoroutineRule.resumeDispatcher()

            // loading is done
            Truth.assertThat(gitResultsDetailsViewModel.dataLoading.getOrAwaitValue()).isFalse()

            // If isDataLoadingError response was error
            Truth.assertThat(gitResultsDetailsViewModel.isDataLoadingError.value).isEqualTo(true)
        }
    }

}