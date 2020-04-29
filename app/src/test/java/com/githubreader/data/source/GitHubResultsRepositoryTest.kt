package com.githubreader.data.source

import androidx.lifecycle.LiveData
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject
import com.githubreader.data.models.Result
import com.githubreader.data.source.local.GitHubResultsLocalDataSource
import com.githubreader.data.source.remote.GitHubResultsRemoteDataSource
import com.githubreader.util.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import java.lang.Exception

/**
 * @author Tomislav Curis
 */

@ExperimentalCoroutinesApi
class GitHubResultsRepositoryTest : GitHubResultsRepository{

    // Dataset
    private val ownerObject = OwnerObject("User1","")
    private val ownerObject2 = OwnerObject("User2","Repo2")
    private val ownerObject3 = OwnerObject("User3","Repo3")
    private val remoteSubscribers = listOf(ownerObject, ownerObject3).sortedBy { it.userName }
    private val localSubscribers = listOf(ownerObject2, ownerObject3).sortedBy { it.userName }


    private val repoObject = RepoObject(1,"Repo1")
    private val repoObject2 = RepoObject(2,"Repo2")
    private val repoObject3 = RepoObject(3,"Repo3")
    private val remoteRepos = listOf(repoObject, repoObject3).sortedBy { it.repoId }
    private val localRepos = listOf(repoObject2, repoObject3).sortedBy { it.repoId }


    private lateinit var gitHubResultsRemoteDataSource: FakeDataSource
    private lateinit var gitHubResultsLocalDataSource: FakeDataSource

    private lateinit var repository: GitHubResultsRepositoryImpl

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun createRepository() {
        gitHubResultsLocalDataSource = FakeDataSource(localRepos.toMutableList(), localSubscribers.toMutableList())
        gitHubResultsRemoteDataSource = FakeDataSource(remoteRepos.toMutableList(), remoteSubscribers.toMutableList())

        repository = GitHubResultsRepositoryImpl(gitHubResultsLocalDataSource, gitHubResultsRemoteDataSource)
    }

    override fun observeRepos(repoName: String): LiveData<Result<List<RepoObject>>> {
        TODO("Not yet implemented")
    }

    override fun observeSubscribers(repoName: String): LiveData<Result<List<OwnerObject>>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveGitHubResultsDB(repoName: String, githubResults: List<RepoObject>) {
        TODO("Not yet implemented")
    }

    override suspend fun getGitHubResults(update: Boolean, repoName: String, page: Int, per_page: Int): Result<List<RepoObject>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveGitHubResultSubscribersDB(repoName: String, subscribers: List<OwnerObject>) {
        TODO("Not yet implemented")
    }

    override suspend fun getGitHubResultSubscribers(update: Boolean, repoName: String, page: Int, per_page: Int): Result<List<OwnerObject>> {
        TODO("Not yet implemented")
    }
}