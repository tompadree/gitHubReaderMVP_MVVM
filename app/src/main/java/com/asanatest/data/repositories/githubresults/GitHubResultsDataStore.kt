package com.asanatest.data.repositories.githubresults

import com.asanatest.data.models.RepoObject
import com.asanatest.data.models.ReposModel
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by Tom on 22.5.2018..
 */
interface GitHubResultsDataStore {

    fun saveGitHubResultsDB(repoName: String, githubResults: ArrayList<RepoObject>): Single<LongArray>

    fun getGitHubResults(repoName: String, page: String, per_page: String): Flowable<ReposModel>

    fun saveGitHubResultDB(repoName: String): Single<LongArray>

    fun getGitHubResultSubscribers(repoId : Int, repoName: String, page: String, per_page: String): Flowable<ArrayList<RepoObject.Owner>>
}