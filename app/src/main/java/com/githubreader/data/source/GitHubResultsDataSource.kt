package com.githubreader.data.source


import androidx.lifecycle.LiveData
import com.githubreader.data.models.*
import com.githubreader.data.models.Result
import com.githubreader.data.models.RepoObject

/**
 * Created by Tom on 22.5.2018..
 */
interface GitHubResultsDataSource {

    fun observeRepos(repoName: String = "a"): LiveData<Result<List<RepoObject>>>

    fun observeSubscribers(repoName: String): LiveData<Result<List<OwnerObject>>>

    suspend fun saveGitHubResultsDB(repoName: String, githubResults: List<RepoObject>)

    suspend fun getGitHubResults(repoName: String = "a", page: Int, per_page: Int): Result<List<RepoObject>>

    suspend fun refreshRepos(repoName: String, page: Int, per_page: Int)

    suspend fun saveGitHubResultSubscribersDB(repoName: String, subscribers: List<OwnerObject>)

    suspend fun getGitHubResultSubscribers(repoName: String, page: Int, per_page: Int): Result<List<OwnerObject>>
}