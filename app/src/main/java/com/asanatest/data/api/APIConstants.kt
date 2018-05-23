package com.asanatest.data.api

/**
 * Created by Tom on 22.5.2018..
 */
interface APIConstants {

    companion object {

        const val BASE_URL = "https://api.github.com/"


        //String API_URL_GET_SEARCH = API_BASE_URL + "search/repositories?q=";//drek";
        const val API_URL_GET_SEARCH = "/search/repositories" //?q="" + repoSearch + "&page=" + page + "&per_page=100";
        const val API_URL_GET_REPO = "/repos"



        const val CONTENT_TYPE = "Content-Type: application/json; charset=utf-8"
        //    String ACCEPT_TYPE = "Accept: application/vnd.oauth.v1+json";
        const val ACCEPT_TYPE_V2 = "Accept: application/json, */*"
        const val CONTENT_TYPE_LABEL = "Content-Type";
        const val CONTENT_TYPE_VALUE_JSON = "application/json; charset=utf-8";


        val httpMethod_GET = "GET"
        val httpMethod_POST = "POST"
        val httpMethod_DELETE = "DELETE"

        val PAGE_ENTRIES = 50

        const val FILTER_FIELD_EMIS = "emisCode"
        const val RPT_ID = "hr2014abs";

    }
}