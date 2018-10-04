package com.githubreader.data.api

import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.ReposModel
import io.reactivex.Flowable

/**
 * Created by Tom on 22.5.2018..
 */
interface NetworkApi {

    fun searchRepos(repoName: String, page: String, per_page: String): Flowable<ReposModel>

    fun getRepoSubscribers(repoName: String, page: String, per_page: String): Flowable<ArrayList<OwnerObject>>

}