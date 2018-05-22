package com.asanatest.data.repositories.githubresults

import com.asanatest.data.api.NetworkApi
import com.asanatest.data.models.RepoObject
import com.asanatest.data.models.ReposModel
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Tom on 22.5.2018..
 */
class RemoteGithubResultsDataStore
@Inject
constructor(private val networkApi: NetworkApi) : GitHubResultsDataStore {

    override fun saveGitHubResultsDB(repoName: String, githubResults: ArrayList<RepoObject>): Single<LongArray> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getGitHubResults(repoName: String, page: String, per_page: String): Flowable<ReposModel> {
        return networkApi.searchRepos(repoName, page, per_page)

    }

    override fun saveGitHubResultDB(repoName: String): Single<LongArray> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getGitHubResultSubscribers(repoId: Int, repoName: String, page: String, per_page: String): Flowable<ArrayList<RepoObject.Owner>> {
        return networkApi.getRepoSubscribers(repoName, page, per_page)
    }
}