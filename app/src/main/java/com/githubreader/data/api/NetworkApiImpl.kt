package com.githubreader.data.api

import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.ReposModel
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Created by Tom on 22.5.2018..
 */
class NetworkApiImpl
@Inject constructor(private val githubApi: GithubApi) : NetworkApi {

    override fun searchRepos(repoName: String, page: String, per_page: String): Flowable<ReposModel> {
        return Flowable.defer<ReposModel> { githubApi.searchRepos(repoName, page, per_page) }
    }

    override fun getRepoSubscribers(repoName: String, page: String, per_page: String): Flowable<ArrayList<OwnerObject>> {
        return Flowable.defer<ArrayList<OwnerObject>> { githubApi.getRepoSubscribers(repoName, page, per_page) }
    }
}