package com.githubreader.data.source

import androidx.lifecycle.LiveData
import com.currencytrackingapp.utils.wrapEspressoIdlingResource
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject
import com.githubreader.data.models.Result
import com.githubreader.data.models.Result.Error
import com.githubreader.data.models.Result.Success
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * @author Tomislav Curis
 */
class GitHubResultsRepositoryImpl(
    private val gitHubResultsLocalDataSource: GitHubResultsDataSource,
    private val gitHubResultsRemoteDataSource: GitHubResultsDataSource) : GitHubResultsRepository {

    override fun observeRepos(repoName: String): LiveData<Result<List<RepoObject>>> {
        wrapEspressoIdlingResource {
            return gitHubResultsLocalDataSource.observeRepos(repoName)
        }
    }

    override fun observeSubscribers(repoName: String): LiveData<Result<List<OwnerObject>>> {
        wrapEspressoIdlingResource {
            return gitHubResultsLocalDataSource.observeSubscribers(repoName)
        }
    }

    override suspend fun getGitHubResults(update: Boolean, repoName: String, page: Int, per_page: Int): Result<List<RepoObject>> {
        wrapEspressoIdlingResource {
            if (update)
                try {
                    updateGitHubResultsFromRemote(repoName, page, per_page)
                } catch (e: Exception) {
                    return Error(e)
                }

            return gitHubResultsLocalDataSource.getGitHubResults(repoName, page, per_page)
        }
    }

    // Only for test
    override suspend fun saveGitHubResultsDB(repoName: String, githubResults: List<RepoObject>) {
        coroutineScope {
            try {
                launch { gitHubResultsLocalDataSource.saveGitHubResultsDB(githubResults) }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun saveGitHubResultSubscribersDB(repoName: String, subscribers: List<OwnerObject>) {
        coroutineScope {
            try {
                launch { gitHubResultsLocalDataSource.saveGitHubResultSubscribersDB(repoName, subscribers) }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun getGitHubResultSubscribers(update: Boolean, repoName: String, page: Int, per_page: Int): Result<List<OwnerObject>> {
        wrapEspressoIdlingResource {
            if (update)
                try {
                    updateGitHubResultSubscribersFromRemote(repoName, page, per_page)
                } catch (e: Exception) {
                    return Error(e)
                }

            return gitHubResultsLocalDataSource.getGitHubResultSubscribers(repoName, page, per_page)
        }
    }

    private suspend fun updateGitHubResultsFromRemote(repoName: String, page: Int, per_page: Int) {
        wrapEspressoIdlingResource {
            val remoteRepos = gitHubResultsRemoteDataSource.getGitHubResults(repoName, page, per_page)
            if(remoteRepos is Success){
                gitHubResultsLocalDataSource.saveGitHubResultsDB(remoteRepos.data)
            } else if(remoteRepos is Result.Error){
                throw remoteRepos.exception
            }
        }

    }

    private suspend fun updateGitHubResultSubscribersFromRemote(repoName: String, page: Int, per_page: Int) {
        wrapEspressoIdlingResource {
            val remoteRepos = gitHubResultsRemoteDataSource.getGitHubResultSubscribers(repoName, page, per_page)
            if(remoteRepos is Success){
                gitHubResultsLocalDataSource.saveGitHubResultSubscribersDB(repoName, remoteRepos.data)
            } else if(remoteRepos is Error){
                throw remoteRepos.exception
            }
        }

    }
}