package com.githubreader.data.source

import androidx.lifecycle.LiveData
import com.currencytrackingapp.utils.wrapEspressoIdlingResource
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject
import com.githubreader.data.models.Result
import com.githubreader.data.source.local.GitHubResultsLocalDataSource
import com.githubreader.data.source.remote.GithubResultsRemoteDataSource
import java.lang.Error
import java.lang.Exception

/**
 * @author Tomislav Curis
 */
class GitHubResultsRepositoryImpl(
    private val gitHubResultsLocalDataSource: GitHubResultsLocalDataSource,
    private val githubResultsRemoteDataSource: GithubResultsRemoteDataSource) : GitHubResultsRepository {

    override fun observeRepos(): LiveData<Result<List<RepoObject>>> {
        wrapEspressoIdlingResource {
            return gitHubResultsLocalDataSource.observeRepos()
        }
    }



    override suspend fun getGitHubResults(
        update: Boolean,
        repoName: String,
        page: Int,
        per_page: Int
    ): Result<List<RepoObject>> {
        if(update)
            try {
                updateGitHubResultsFromRemote(repoName, page, per_page)
            } catch (e: Exception) {
                throw Error(e)
            }

        return gitHubResultsLocalDataSource.getGitHubResults(repoName, page, per_page)
    }

    // Only for test
    override suspend fun saveGitHubResultsDB(repoName: String, githubResults: List<RepoObject>) {
        TODO("Not yet implemented")
    }

    override suspend fun saveGitHubResultSubscribersDB(
        repoName: String,
        subscribers: List<OwnerObject>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getGitHubResultSubscribers(
        update: Boolean,
        repoName: String,
        page: Int,
        per_page: Int
    ): Result<List<OwnerObject>> {
        if(update)
            try {
                updateGitHubResultSubscribersFromRemote(repoName, page, per_page)
            } catch (e: Exception) {
                throw Error(e)
            }

        return gitHubResultsLocalDataSource.getGitHubResultSubscribers(repoName, page, per_page)
    }

    private suspend fun updateGitHubResultsFromRemote(
        repoName: String,
        page: Int,
        per_page: Int
    ) {
        wrapEspressoIdlingResource {
            val remoteRepos = githubResultsRemoteDataSource.getGitHubResults(repoName, page, per_page)
            if(remoteRepos is Result.Success){
                gitHubResultsLocalDataSource.saveGitHubResultsDB(repoName, remoteRepos.data)
            } else if(remoteRepos is Result.Error){
                throw remoteRepos.exception
            }
        }

    }

    private suspend fun updateGitHubResultSubscribersFromRemote(
        repoName: String,
        page: Int,
        per_page: Int
    ) {
        wrapEspressoIdlingResource {
            val remoteRepos = githubResultsRemoteDataSource.getGitHubResults(repoName, page, per_page)
            if(remoteRepos is Result.Success){
                gitHubResultsLocalDataSource.saveGitHubResultsDB(repoName, remoteRepos.data)
            } else if(remoteRepos is Result.Error){
                throw remoteRepos.exception
            }
        }

    }
}