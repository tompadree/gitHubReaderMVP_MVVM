package com.githubreader.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject
import com.githubreader.data.models.Result
import com.githubreader.data.source.GitHubResultsDataSource
import com.githubreader.data.source.remote.api.APIConstants.Companion.SORT_STARS
import com.githubreader.data.source.remote.api.GithubApi
import java.io.IOException


/**
 * @author Tomislav Curis
 */
class GitHubResultsRemoteDataSource(private val githubApi: GithubApi): GitHubResultsDataSource {

    private val observableRepos = MutableLiveData<Result<List<RepoObject>>>()

    private val observableOwners = MutableLiveData<Result<List<OwnerObject>>>()

    override fun observeRepos(repoName: String): LiveData<Result<List<RepoObject>>> = observableRepos

    override fun observeSubscribers(repoName: String): LiveData<Result<List<OwnerObject>>> = observableOwners

    override suspend fun getGitHubResults(repoName: String, page: Int, per_page: Int): Result<List<RepoObject>> {
        val response =
            githubApi.searchRepos(repoName, SORT_STARS, page.toString(), per_page.toString())
        if (response.isSuccessful) {
            val body = response.body()
            if (response.body() != null) {
                val result = Result.Success(body!!.items)
                observableRepos.value = result
                return result
            }
        }
        return Result.Error(IOException("Error loading data " + "${response.code()} ${response.message()}"))
    }

    override suspend fun refreshRepos(repoName: String, page: Int, per_page: Int) {
//        observableRepos.value = getGitHubResults()
    }

    override suspend fun getGitHubResultSubscribers(repoName: String, page: Int, per_page: Int)
            : Result<List<OwnerObject>> {
        val response = githubApi.getRepoSubscribers(repoName, page.toString(), per_page.toString())
        if(response.isSuccessful){
            val body = response.body()
            if(response.body() != null){
                val result = Result.Success(body!!)
                observableOwners.value = result
                return result
            }
        }
        return Result.Error(IOException("Error loading data " + "${response.code()} ${response.message()}"))
    }


    override suspend fun saveGitHubResultsDB(
        repoName: String,
        githubResults: List<RepoObject>
    ) {
        TODO("Not yet implemented")
    }
    override suspend fun saveGitHubResultSubscribersDB(
        repoName: String,
        subscribers: List<OwnerObject>
    ) {
        TODO("Not yet implemented")
    }
}
