package com.asanatest.data.api

import com.asanatest.data.models.OwnerObject
import com.asanatest.data.models.RepoObject
import com.asanatest.data.models.ReposModel
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by Tom on 22.5.2018..
 */
interface NetworkApi {

    fun searchRepos(repoName: String, page: String, per_page: String): Flowable<ReposModel>

    fun getRepoSubscribers(repoName: String, page: String, per_page: String): Flowable<ArrayList<OwnerObject>>

}