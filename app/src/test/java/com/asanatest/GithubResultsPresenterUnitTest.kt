package com.githubreader

import android.content.Context
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject
import com.githubreader.domain.interactors.GithubResultsInteractor
import com.githubreader.domain.listeners.OnResultFetchListener
import com.githubreader.view.views.GitResultsView
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Tom on 23.5.2018..
 */
//@RunWith(MockitoJUnitRunner::class)
class GithubResultsPresenterUnitTest : OnResultFetchListener {

    private val DUMMY_SEARCH = "a"

    @Mock
    private val mockContext: Context? = null

    @Mock
    lateinit var gitResultsView: GitResultsView

    @Mock
    lateinit var interactor : GithubResultsInteractor


    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun fetchRepoSubscribers(repoId: Int, repoName: String) {
        gitResultsView.showLoading()
        interactor.getGitHubResultSubscribers(repoId, repoName, this)
    }

    @Test
    fun fetchRepos(repoName: String) {
        gitResultsView.showLoading()
        interactor.getGitHubResults(repoName, this)
    }

    @Test
    override fun onRepoSubscribersFetched(subscribers: ArrayList<OwnerObject>) {
        gitResultsView.repoSubscribersFetched(subscribers)
        gitResultsView.hideLoading()
    }

    @Test
    override fun onReposFetched(repos: ArrayList<RepoObject>) {
        gitResultsView.updateList(repos)
    }

    @Test
    override fun onReposError(e: Throwable) {
        gitResultsView.showError(e.localizedMessage)
        gitResultsView.hideLoading()
    }

    @Test
    fun fetchNextPage() {
        interactor.fetchNextPage()
    }

    @Test
    override fun showLoadingFooter() {
        gitResultsView.showLoadingFooter()
    }

    @Test
    override fun hideLoadingFooter() {
        gitResultsView.hideLoadingFooter()
    }

    @Test
    override fun showLoading() {
        gitResultsView.showLoading()
    }

    @Test
    override fun hideLoading() {
        gitResultsView.hideLoading()
    }

//    @Test
//    fun testUserListPresenterSearch() {
//
//        given(gitResultsView!!).willReturn(gitResultsView)
//
//        verify(githubResultsPresenter!!).fetchRepos(DUMMY_SEARCH)
//
//        verify<GitResultsView>(gitResultsView).showLoading()
//
//        verify(githubResultsPresenter!!).fetchRepoSubscribers(eq<Int>(1),eq<String>(DUMMY_SEARCH))
//    }

}