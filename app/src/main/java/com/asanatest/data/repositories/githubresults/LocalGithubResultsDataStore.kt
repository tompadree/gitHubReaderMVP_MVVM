package com.asanatest.data.repositories.githubresults

import com.asanatest.data.db.GitHubDAO
import com.asanatest.data.db.GitHubDatabase
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
constructor(private val database: GitHubDatabase) : GitHubResultsDataStore {

    private val dao: GitHubDAO = database.getGitHubDao()

    override fun saveGitHubResultsDB(repoName: String, githubResults: ArrayList<RepoObject>): Single<LongArray> {
        for (i in 0 until githubResults.size)
            githubResults[i].from_cache = true
        return Single.fromCallable { dao.saveGitHubResults(githubResults) }
    }

    override fun getGitHubResults(repoName: String, page: Int, per_page: Int): Flowable<ReposModel> {
        val reposModel = ReposModel()
        return Single.fromCallable { ArrayList(dao.getGitHubResults("%$repoName%", ((page - 1) * per_page), per_page)) }.toFlowable()
                .map {
                    reposModel.items = it
                    reposModel
                }
    }

    override fun saveGitHubResultSubscribersDB(repoName: String, subscribers: ArrayList<OwnerObject>): Single<LongArray> {
        for (i in 0 until subscribers.size)
            subscribers[i].parentRepo = repoName
        return Single.fromCallable { dao.saveGitHubResultSubscribers(subscribers) }
    }

    override fun getGitHubResultSubscribers(repoId: Int, repoName: String, page: Int, per_page: Int): Flowable<ArrayList<OwnerObject>> {
        return Single.fromCallable { ArrayList(dao.getGitHubResultSubscribers("%$repoName%", ((page - 1) * per_page), per_page)) }.toFlowable()
    }
}