package com.asanatest.data.db;

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import com.asanatest.data.models.OwnerObject
import com.asanatest.data.models.RepoObject
import com.asanatest.data.models.ReposModel
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Tomislav on 23,May,2018
 */
class RoomGitHubCache(database: GitHubDatabase) : GitHubCache {

    private val dao: GitHubDAO = database.getGitHubDao()

    override fun saveGitHubResults(githubResults: ArrayList<RepoObject>): Single<LongArray> {
        return Single.fromCallable { dao.saveGitHubResults(githubResults) }
    }

//    fun getResults() {
//
//        resultList = dao.results().create(INITIAL_LOAD_KEY, PagedList.Config.Builder()
//                .setPageSize(PAGE_SIZE)
//                .setPrefetchDistance(PREFETCH_DISTANCE)
//                .setEnablePlaceholders(true)
//                .build())
//
//    }
//
//    fun <T> LiveData<T>.toFlowable(lifecycleOwner: LifecycleOwner) : Flowable<T> =
//            Flowable.fromPublisher(LiveDataReactiveStreams.toPublisher(lifecycleOwner, this))


    override fun getGitHubResults(repoName: String, page: Int, per_page: Int): Single<ArrayList<RepoObject>> {
        val sending = "%$repoName%"
        return Single.fromCallable { ArrayList(dao.getGitHubResults(sending)) }

    }

    override fun saveGitHubResultSubscribersDB(subscribers: ArrayList<OwnerObject>): Single<LongArray> {
        return Single.fromCallable { dao.saveGitHubResultSubscribers(subscribers) }
    }

    override fun getGitHubResultSubscribers(repoId: Int, repoName: String, page: Int, per_page: Int): Single<ArrayList<OwnerObject>> {
        return Single.fromCallable { ArrayList(dao.getGitHubResultSubscribers("%$repoName%")) }//, page, per_page)) }
    }
}