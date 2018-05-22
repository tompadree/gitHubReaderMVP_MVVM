package com.asanatest.domain.interactors

import com.asanatest.domain.listeners.OnResultFetchListener

/**
 * Created by Tom on 22.5.2018..
 */
interface GithubResultsInteractor {


    fun getGitHubResults(repoName: String, listener: OnResultFetchListener)

    fun getGitHubResultSubscribers(repoId : Int, repoName: String, listener: OnResultFetchListener)

    fun fetchNextPage()

    fun destroyDisposable()

}