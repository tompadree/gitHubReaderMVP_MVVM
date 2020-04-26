package com.githubreader.data.source.remote.api

import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.ReposModel
import com.githubreader.data.source.remote.api.APIConstants.Companion.API_URL_GET_REPO
import com.githubreader.data.source.remote.api.APIConstants.Companion.API_URL_GET_SEARCH
import com.githubreader.data.source.remote.api.APIConstants.Companion.CONTENT_TYPE
import retrofit2.http.*
import retrofit2.Response

/**
 * Created by Tom on 22.5.2018..
 */
interface GithubApi {

    @Headers(CONTENT_TYPE)
    @GET(API_URL_GET_SEARCH)
    suspend fun searchRepos(@Query("q") repoName: String,
                    @Query("sort") stars: String,
                    @Query("page") page: String,
                    @Query("per_page") per_page: String): Response<ReposModel>


    @Headers(CONTENT_TYPE)
    @GET("$API_URL_GET_REPO/{repoName}/subscribers")
    suspend fun getRepoSubscribers(@Path("repoName", encoded = true) repoName: String,
                           @Query("page") page: String,
                           @Query("per_page") per_page: String): Response<List<OwnerObject>>
}