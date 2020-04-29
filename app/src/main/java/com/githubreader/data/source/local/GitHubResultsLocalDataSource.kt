package com.githubreader.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject
import com.githubreader.data.models.Result
import com.githubreader.data.source.GitHubResultsDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

/**
 * @author Tomislav Curis
 */
class GitHubResultsLocalDataSource(
    private val dao: GitHubDAO,
    private val dispatchers: CoroutineDispatcher = Dispatchers.IO) : GitHubResultsDataSource {

    override fun observeRepos(repoName: String): LiveData<Result<List<RepoObject>>> {
        return dao.observeRepos("%$repoName%").map { Result.Success(it) }
    }

    override fun observeSubscribers(repoName: String): LiveData<Result<List<OwnerObject>>> {
        return dao.observeSubscribers("%$repoName%").map { Result.Success(it) }
    }

    override suspend fun saveGitHubResultsDB(githubResults: List<RepoObject>)
            = withContext(dispatchers) {
//        dao.deleteRepos()
        dao.saveGitHubResults(githubResults)}

    override suspend fun getGitHubResults(repoName: String, page: Int, per_page: Int): Result<List<RepoObject>> =
        withContext(dispatchers) {
            return@withContext try {
                Result.Success(dao.getGitHubResults("%$repoName%", page, per_page))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun refreshRepos(repoName: String, page: Int, per_page: Int) {
        TODO("Not yet implemented")
    }


    override suspend fun saveGitHubResultSubscribersDB(repoName: String, subscribers: List<OwnerObject>
    ) = withContext(dispatchers) {
        // Adding parent repo for id
        for (element in subscribers)
            element.parentRepo = repoName
        dao.deleteOwners()
        dao.saveGitHubResultSubscribers(subscribers)
    }

    override suspend fun getGitHubResultSubscribers(repoName: String, page: Int, per_page: Int
    ): Result<List<OwnerObject>> =
        withContext(dispatchers) {
            return@withContext try {
                Result.Success(dao.getGitHubResultSubscribers("%$repoName%", page, per_page))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
}