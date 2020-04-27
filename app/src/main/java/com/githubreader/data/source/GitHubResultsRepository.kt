package com.githubreader.data.source

import androidx.lifecycle.LiveData
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject
import com.githubreader.data.models.Result

/**
 * @author Tomislav Curis
 */

interface GitHubResultsRepository {

    fun observeRepos(): LiveData<Result<List<RepoObject>>>

    fun observeSubscribers(): LiveData<Result<List<OwnerObject>>>

    suspend fun saveGitHubResultsDB(repoName: String, githubResults: List<RepoObject>)

    suspend fun getGitHubResults(update: Boolean = false, repoName: String, page: Int, per_page: Int): Result<List<RepoObject>>

    suspend fun saveGitHubResultSubscribersDB(repoName: String, subscribers: List<OwnerObject>)

    suspend fun getGitHubResultSubscribers(update: Boolean = false, repoName: String, page: Int, per_page: Int): Result<List<OwnerObject>>
}