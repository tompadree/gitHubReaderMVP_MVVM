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

    private val INITIAL_LOAD_KEY = 0
    private val PREFETCH_DISTANCE = 5

    private val dao: GitHubDAO = database.getGitHubDao()


    var resultList: Flowable<PagedList<RepoObject>>? = null

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

//        var tst = RepoObject()
//        val test : Single<RepoObject> = Single.fromCallable {dao.getGitHubResults1("%$repoName%") }
//        test
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(Schedulers.io())
//                .subscribe(object  : SingleObserver<RepoObject> {
//
//                    override fun onSuccess(t: RepoObject) {
//                        tst = t
//                    }
//
//                    override fun onSubscribe(d: Disposable) {
//
//                    }
//
//                    override fun onError(e: Throwable) {
//                        val er = e
//                    }
//                })

        val sending = "%$repoName%"
        return Single.fromCallable { ArrayList(dao.getGitHubResults(sending)) }

    }

    override fun saveGitHubResultSubscribersDB(subscribers: ArrayList<OwnerObject>): Single<LongArray> {
        return Single.fromCallable { dao.saveGitHubResultSubscribers(subscribers) }
    }

    override fun getGitHubResultSubscribers(repoId: Int, repoName: String, page: Int, per_page: Int): Single<ArrayList<OwnerObject>> {
        var sending = repoName.split("/")[0]
        sending = "%$sending%"
        return Single.fromCallable { ArrayList(dao.getGitHubResultSubscribers("%$repoName%")) }//, page, per_page)) }
    }
}