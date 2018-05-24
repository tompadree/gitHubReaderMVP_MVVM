package com.asanatest.data.repositories.githubresults

import com.asanatest.data.db.GitHubCache
import com.asanatest.data.models.OwnerObject
import com.asanatest.data.models.RepoObject
import com.asanatest.data.models.ReposModel
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Tom on 22.5.2018..
 */
class LocalGithubResultsDataStore
@Inject
constructor(private val gitHubCache: GitHubCache) : GitHubResultsDataStore {

    override fun saveGitHubResultsDB(repoName: String, githubResults: ArrayList<RepoObject>): Single<LongArray> {
        for (i in 0 until githubResults.size)
            githubResults[i].from_cache = true

        return gitHubCache.saveGitHubResults(githubResults)
    }

    override fun getGitHubResults(repoName: String, page: Int, per_page: Int): Flowable<ReposModel> {
        var reposModel = ReposModel()
        return gitHubCache.getGitHubResults(repoName, ((page - 1) * per_page) , per_page).toFlowable()
                .map {
                    reposModel.items = it
                    reposModel
                }
    }

    override fun saveGitHubResultSubscribersDB(repoName: String, subscribers: ArrayList<OwnerObject>): Single<LongArray> {
        for (i in 0 until subscribers.size)
            subscribers[i].parentRepo = repoName

        return gitHubCache.saveGitHubResultSubscribersDB(subscribers)
    }

    override fun getGitHubResultSubscribers(repoId: Int, repoName: String, page: Int, per_page: Int): Flowable<ArrayList<OwnerObject>> {
        return gitHubCache.getGitHubResultSubscribers(repoId, repoName, ((page - 1) * per_page) , per_page).toFlowable()
    }
}