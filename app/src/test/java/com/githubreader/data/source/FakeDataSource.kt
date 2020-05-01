package com.githubreader.data.source

import androidx.lifecycle.LiveData
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject
import com.githubreader.data.models.Result
import java.lang.Exception

/**
 * @author Tomislav Curis
 */
class FakeDataSource(
    var repos: MutableList<RepoObject>? = mutableListOf(),
    var owners: MutableList<OwnerObject>? = mutableListOf()) : GitHubResultsDataSource {

    override fun observeRepos(repoName: String): LiveData<Result<List<RepoObject>>> {
        TODO("Not yet implemented")
    }

    override fun observeSubscribers(repoName: String): LiveData<Result<List<OwnerObject>>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveGitHubResultsDB(githubResults: List<RepoObject>) {
        repos?.clear()
        repos?.addAll(githubResults)
    }

    override suspend fun getGitHubResults(repoName: String, page: Int, per_page: Int): Result<List<RepoObject>> {
        repos?.let { return  Result.Success(ArrayList(it)) }
        return Result.Error(Exception("Repos not found"))
    }

    override suspend fun refreshRepos(repoName: String, page: Int, per_page: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun saveGitHubResultSubscribersDB(repoName: String, subscribers: List<OwnerObject>) {
        owners?.clear()
        owners?.addAll(subscribers)    }

    override suspend fun getGitHubResultSubscribers(repoName: String, page: Int, per_page: Int): Result<List<OwnerObject>> {
        owners?.let { return  Result.Success(ArrayList(it)) }
        return Result.Error(Exception("Repos not found"))    }

    override suspend fun deleteRepos() {
        repos?.clear()
    }

    override suspend fun deleteOwners() {
        owners?.clear()
    }
}