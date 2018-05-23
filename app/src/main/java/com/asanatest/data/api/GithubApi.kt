package com.asanatest.data.api

import com.asanatest.data.api.APIConstants.Companion.API_URL_GET_REPO
import com.asanatest.data.api.APIConstants.Companion.API_URL_GET_SEARCH
import com.asanatest.data.api.APIConstants.Companion.CONTENT_TYPE
import com.asanatest.data.models.OwnerObject
import com.asanatest.data.models.RepoObject
import com.asanatest.data.models.ReposModel
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.*

/**
 * Created by Tom on 22.5.2018..
 */
interface GithubApi {

    @Headers(CONTENT_TYPE)
    @GET(API_URL_GET_SEARCH)
    fun searchRepos(@Query("q") repoName: String,
                    @Query("page") page: String,
                    @Query("per_page") per_page: String): Flowable<ReposModel>


    @Headers(CONTENT_TYPE)
    @GET(API_URL_GET_REPO + "/{repoName}/subscribers")
    fun getRepoSubscribers(@Path("repoName", encoded = true) repoName: String,
                           @Query("page") page: String,
                           @Query("per_page") per_page: String): Flowable<ArrayList<OwnerObject>>
}