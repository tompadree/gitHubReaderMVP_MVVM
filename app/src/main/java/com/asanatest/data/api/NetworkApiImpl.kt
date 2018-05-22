package com.asanatest.data.api

import com.asanatest.data.models.RepoObject
import com.asanatest.data.models.ReposModel
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Tom on 22.5.2018..
 */
class NetworkApiImpl
@Inject constructor(private val githubApi: GithubApi) : NetworkApi {

    override fun searchRepos(repoName: String, page: String, per_page: String): Flowable<ReposModel> {
        return Flowable.defer<ReposModel> { githubApi.searchRepos(repoName, page, per_page) }
    }

    override fun getRepoSubscribers(repoName: String, page: String, per_page: String): Flowable<ArrayList<RepoObject.Owner>> {
        return Flowable.defer<ArrayList<RepoObject.Owner>> { githubApi.getRepoSubscribers(repoName, page, per_page) }
    }
}