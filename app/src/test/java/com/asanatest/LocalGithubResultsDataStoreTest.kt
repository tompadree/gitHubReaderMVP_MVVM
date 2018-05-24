package com.asanatest

import com.asanatest.data.db.GitHubDAO
import com.asanatest.data.models.OwnerObject
import com.asanatest.data.models.RepoObject
import com.asanatest.data.models.ReposModel
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when` as whenever
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import io.reactivex.SingleEmitter
import org.mockito.Mockito.`when`

/**
 * Created by Tomislav on 24,May,2018
 */
class LocalGithubResultsDataStoreTest {


    @Mock
    lateinit var gitHubDAO: GitHubDAO

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
//        userRepository = DefaultUserRepository(mockUserService, mockUserDao, mockConnectionHelper, mockPreferencesHelper, mockCalendarWrapper)
    }

    @Test
    fun testGetUsers_isOnlineReceivedEmptyList_emitEmptyList() {
        // Given
        val userListModel = UserListModel(emptyList())

        // When
        setUpMocks(userListModel, true)
        val testObserver = userRepository.getUsers().test()

        // Then
        testObserver.assertNoErrors()
        testObserver.assertValue { userListModelResult: UserListModel -> userListModelResult.items.isEmpty() }
        verify(mockUserDao).insertAll(userListModel.items)
    }


    https://android.jlelse.eu/complete-example-of-testing-mvp-architecture-with-kotlin-and-rxjava-part-1-816e22e71ff4
    https://github.com/kozmi55/Kotlin-Android-Examples/blob/master/app/src/test/java/com/example/tamaskozmer/kotlinrxexample/domain/interactors/GetUsersTest.kt

    @Test
    fun testExecute_userListModelWithOneItem_emitListWithOneViewModel() {
        // Given
        val mockSingle = Single.create { e: SingleEmitter<UserListModel>? -> e?.onSuccess(UserListModel(listOf(User(1, "Name", 100, "Image url")))) }

        // When
        `when`(mockUserRepository.getUsers(1, false))
                .thenReturn(mockSingle)

        val resultSingle = getUsers.execute(1, false)

        val testObserver = resultSingle.test()

        testObserver.assertNoErrors()
        testObserver.assertValue { userViewModels: List<UserViewModel> -> userViewModels.size == 1 }
        testObserver.assertValue { userViewModels: List<UserViewModel> ->
            userViewModels.get(0) == UserViewModel(1, "Name", 100, "Image url") }
    }


    fun saveGetGitHubResultsDB(repoName: String, githubResults: ArrayList<RepoObject>): Single<LongArray> {
        for (i in 0 until githubResults.size)
            githubResults[i].from_cache = true
        return Single.fromCallable { dao.saveGitHubResults(githubResults) }
    }

    override fun getGitHubResults(repoName: String, page: Int, per_page: Int): Flowable<ReposModel> {
        var reposModel = ReposModel()
        return Single.fromCallable { ArrayList(dao.getGitHubResults("%$repoName%", ((page - 1) * per_page), per_page)) }.toFlowable()
                .map {
                    reposModel.items = it
                    reposModel
                }
    }

    fun saveGetGitHubResultSubscribers(repoName: String, subscribers: ArrayList<OwnerObject>): Single<LongArray> {
        for (i in 0 until subscribers.size)
            subscribers[i].parentRepo = repoName
        return Single.fromCallable { dao.saveGitHubResultSubscribers(subscribers) }
    }

    override fun getGitHubResultSubscribers(repoId: Int, repoName: String, page: Int, per_page: Int): Flowable<ArrayList<OwnerObject>> {
        return Single.fromCallable { ArrayList(dao.getGitHubResultSubscribers("%$repoName%", ((page - 1) * per_page), per_page)) }.toFlowable()
    }

    private fun setUpMocks(modelFromUserService: UserListModel, isOnline: Boolean) {
        `when`(mockConnectionHelper.isOnline()).thenReturn(isOnline)
        `when`(mockCalendarWrapper.getCurrentTimeInMillis()).thenReturn(1000 * 60 * 60 * 12 + 1)
        `when`(mockPreferencesHelper.loadLong("last_update_page_1")).thenReturn(0)

        `when`(mockUserService.getUsers()).thenReturn(mockUserCall)
        `when`(mockUserCall.execute()).thenReturn(mockUserResponse)
        `when`(mockUserResponse.body()).thenReturn(modelFromUserService)
        `when`(mockUserDao.getUsers(1)).thenReturn(emptyList())
    }

}