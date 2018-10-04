package com.githubreader.data.api

/**
 * Created by Tom on 22.5.2018..
 */
interface APIConstants {

    companion object {

        const val BASE_URL = "https://api.github.com"

        const val API_URL_GET_SEARCH = "/search/repositories"
        const val API_URL_GET_REPO = "/repos"

        const val DUMMY_SEARCH = "created:>2018-09-01"

        const val CONTENT_TYPE = "Content-Type: application/json; charset=utf-8"

        val PAGE_ENTRIES = 50
        val SORT_STARS = "stars"


    }
}