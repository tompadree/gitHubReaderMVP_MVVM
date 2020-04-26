package com.githubreader.presenter.impl

import android.content.Context
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject
import com.githubreader.domain.interactors.GithubResultsInteractor
import com.githubreader.domain.listeners.OnResultFetchListener
import com.githubreader.presenter.GithubResultsPresenter
import com.githubreader.view.views.GitResultsView

/**
 * Created by Tom on 22.5.2018..
 */
class GithubResultsPresenterImpl

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

    override fun showLoading() {
        gitResultsView.showLoading()
    }

    override fun hideLoading() {
        gitResultsView.hideLoading()
    }

    override fun resume() {
    }

    override fun pause() {
    }

    override fun destroy() {
        interactor.destroyDisposable()
    }
}