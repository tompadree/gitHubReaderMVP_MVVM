package com.githubreader.domain.interactors

import com.githubreader.domain.listeners.OnResultFetchListener

/**
 * Created by Tom on 22.5.2018..
 */
interface GithubResultsInteractor {


    fun getGitHubResults(repoName: String, listener: OnResultFetchListener)

    fun getGitHubResultSubscribers(repoId : Int, repoName: String, listener: OnResultFetchListener)

    fun fetchNextPage()

    fun destroyDisposable()

}