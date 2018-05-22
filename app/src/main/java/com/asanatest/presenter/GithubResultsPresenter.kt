package com.asanatest.presenter

/**
 * Created by Tom on 22.5.2018..
 */
interface GithubResultsPresenter : BasePresenter {

    fun fetchRepos(repoName : String)

    fun fetchNextPage()

    fun fetchRepoSubscribers(repoId : Int, repoName : String)
}