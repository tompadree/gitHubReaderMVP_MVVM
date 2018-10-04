package com.githubreader.data.api

import com.githubreader.data.api.APIConstants.Companion.API_URL_GET_REPO
import com.githubreader.data.api.APIConstants.Companion.API_URL_GET_SEARCH
import com.githubreader.data.api.APIConstants.Companion.CONTENT_TYPE
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.ReposModel
import io.reactivex.Flowable
import retrofit2.http.*

/**
 * Created by Tom on 22.5.2018..
 */
interface GithubApi {

    @Headers(CONTENT_TYPE)
    @GET(API_URL_GET_SEARCH)
    fun searchRepos(@Query("q") repoName: String,
                    @Query("sort") stars: String,
                    @Query("page") page: String,
                    @Query("per_page") per_page: String): Flowable<ReposModel>


    @Headers(CONTENT_TYPE)
    @GET(API_URL_GET_REPO + "/{repoName}/subscribers")
    fun getRepoSubscribers(@Path("repoName", encoded = true) repoName: String,
                           @Query("page") page: String,
                           @Query("per_page") per_page: String): Flowable<ArrayList<OwnerObject>>
}