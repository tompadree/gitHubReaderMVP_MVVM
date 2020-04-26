package com.githubreader.data.repositories.githubresults

import com.githubreader.data.api.NetworkApi
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject
import com.githubreader.data.models.ReposModel
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by Tom on 22.5.2018..
 */
class RemoteGithubResultsDataStore

constructor(private val networkApi: NetworkApi) : GitHubResultsDataStore {

    override fun saveGitHubResultsDB(repoName: String, githubResults: ArrayList<RepoObject>): Single<LongArray> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getGitHubResults(repoName: String, page: Int, per_page: Int): Flowable<ReposModel> {
        return networkApi.searchRepos(repoName, page.toString(), per_page.toString())

    }

    override fun saveGitHubResultSubscribersDB(repoName: String, subscribers: ArrayList<OwnerObject>): Single<LongArray> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getGitHubResultSubscribers(repoId: Int, repoName: String, page: Int, per_page: Int): Flowable<ArrayList<OwnerObject>> {
        return networkApi.getRepoSubscribers(repoName, page.toString(), per_page.toString())
    }
}