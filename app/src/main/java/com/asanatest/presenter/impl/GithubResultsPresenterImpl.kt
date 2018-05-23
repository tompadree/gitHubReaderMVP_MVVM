package com.asanatest.presenter.impl

import android.content.Context
import com.asanatest.data.models.OwnerObject
import com.asanatest.data.models.RepoObject
import com.asanatest.domain.interactors.GithubResultsInteractor
import com.asanatest.domain.listeners.OnResultFetchListener
import com.asanatest.presenter.GithubResultsPresenter
import com.asanatest.view.views.GitResultsView
import javax.inject.Inject

/**
 * Created by Tom on 22.5.2018..
 */
class GithubResultsPresenterImpl
@Inject
constructor(private val context: Context, private val interactor: GithubResultsInteractor,
            private val gitResultsView: GitResultsView) : GithubResultsPresenter, OnResultFetchListener {

    override fun fetchRepoSubscribers(repoId: Int, repoName: String) {
        gitResultsView.showLoading()
        interactor.getGitHubResultSubscribers(repoId, repoName, this)
    }

    override fun fetchRepos(repoName: String) {
        gitResultsView.showLoading()
        interactor.getGitHubResults(repoName, this)
    }

    override fun onRepoSubscribersFetched(subscribers: ArrayList<OwnerObject>) {
        gitResultsView.repoSubscribersFetched(subscribers)
        gitResultsView.hideLoading()
    }

    override fun onReposFetched(repos: ArrayList<RepoObject>) {
        gitResultsView.updateList(repos)
    }

    override fun onReposError(e: Throwable) {
        gitResultsView.showError(e.localizedMessage)
        gitResultsView.hideLoading()
    }

    override fun fetchNextPage() {
        interactor.fetchNextPage()
    }

    override fun showLoadingFooter() {
        gitResultsView.showLoadingFooter()
    }

    override fun hideLoadingFooter() {
        gitResultsView.hideLoadingFooter()
    }


    override fun resume() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pause() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun destroy() {
        interactor.destroyDisposable()
    }
}