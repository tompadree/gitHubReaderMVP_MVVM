package com.asanatest.data.repositories.githubresults

import com.asanatest.data.models.OwnerObject
import com.asanatest.data.models.RepoObject
import com.asanatest.data.models.ReposModel
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by Tom on 22.5.2018..
 */
interface GitHubResultsDataStore {

    fun saveGitHubResultsDB(repoName: String, githubResults: ArrayList<RepoObject>): Single<LongArray>

    fun getGitHubResults(repoName: String, page: Int, per_page: Int): Flowable<ReposModel>

    fun saveGitHubResultSubscribersDB(repoName: String, subscribers: ArrayList<OwnerObject>): Single<LongArray>

    fun getGitHubResultSubscribers(repoId : Int, repoName: String, page: Int, per_page: Int): Flowable<ArrayList<OwnerObject>>
}