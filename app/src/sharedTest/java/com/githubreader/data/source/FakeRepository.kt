package com.githubreader.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject
import com.githubreader.data.models.Result
import kotlinx.coroutines.runBlocking

/**
 * @author Tomislav Curis
 */
class FakeRepository : GitHubResultsRepository{

    var currentListRepos: List<RepoObject> = mutableListOf()
    var currentListOwners: List<OwnerObject> = mutableListOf()

    private var shouldReturnError = false

    private val observableRepos = MutableLiveData<Result<List<RepoObject>>>()
    private val observableOwners = MutableLiveData<Result<List<OwnerObject>>>()

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override fun observeRepos(repoName: String): LiveData<Result<List<RepoObject>>> {
        runBlocking { observableRepos.value = Result.Success(currentListRepos) }
        return observableRepos
    }

    override fun observeSubscribers(repoName: String): LiveData<Result<List<OwnerObject>>> {
        runBlocking { observableOwners.value = Result.Success(currentListOwners) }
        return observableOwners
    }

    override suspend fun saveGitHubResultsDB(repoName: String, githubResults: List<RepoObject>) {
        (currentListRepos as ArrayList).clear()
        (currentListRepos as ArrayList).addAll(githubResults)
    }

    override suspend fun getGitHubResults(update: Boolean, repoName: String, page: Int, per_page: Int): Result<List<RepoObject>> {
        if (shouldReturnError) {
            return Result.Error(Exception("Test exception"))
        }
//        runBlocking { observableRepos.value = Result.Success(currentListRepos.values.toList()) }

        return Result.Success(currentListRepos)
    }

    override suspend fun saveGitHubResultSubscribersDB(repoName: String, subscribers: List<OwnerObject>) {
        (currentListOwners as ArrayList).clear()
        (currentListOwners as ArrayList).addAll(subscribers)
    }

    override suspend fun getGitHubResultSubscribers(update: Boolean, repoName: String, page: Int, per_page: Int): Result<List<OwnerObject>> {
        if (shouldReturnError) {
            return Result.Error(Exception("Test exception"))
        }
//        runBlocking { observableOwners.value = Result.Success(currentListOwners.values.toList()) }

        return Result.Success(currentListOwners)
    }
}