package com.asanatest

import android.content.Context
import com.asanatest.data.models.OwnerObject
import com.asanatest.data.models.RepoObject
import com.asanatest.domain.interactors.GithubResultsInteractor
import com.asanatest.domain.listeners.OnResultFetchListener
import com.asanatest.presenter.GithubResultsPresenter
import com.asanatest.view.views.GitResultsView
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.ArgumentMatchers.eq
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import org.reactivestreams.Subscriber

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