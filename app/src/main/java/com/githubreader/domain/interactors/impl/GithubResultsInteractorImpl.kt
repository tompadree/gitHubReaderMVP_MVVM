package com.githubreader.domain.interactors.impl

import android.content.Context
import com.githubreader.data.api.APIConstants.Companion.PAGE_ENTRIES
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject
import com.githubreader.data.repositories.githubresults.LocalGithubResultsDataStore
import com.githubreader.data.repositories.githubresults.RemoteGithubResultsDataStore
import com.githubreader.domain.interactors.GithubResultsInteractor
import com.githubreader.domain.listeners.OnResultFetchListener
import com.githubreader.utils.helpers.NetworkHelper
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import kotlin.collections.ArrayList

/**
 * Created by Tom on 22.5.2018..
 */
class GithubResultsInteractorImpl
 constructor(private val context: Context, private val localGithubResultsDataStore: LocalGithubResultsDataStore,
                    private val remoteGithubResultsDataStore: RemoteGithubResultsDataStore) : GithubResultsInteractor {

//    @field:Named(ThreadModule.SUBSCRIBE_SCHEDULER)
    lateinit var subscribeScheduler: Scheduler

//    @field:Named(ThreadModule.OBSERVE_SCHEDULER)
    lateinit var observeScheduler: Scheduler

    private var currentPage: Int = 0
    private val disposables: CompositeDisposable? = null
    private lateinit var paginator: PublishProcessor<Int>
    private var loading: Boolean = false

    override fun getGitHubResults(repoName: String, listener: OnResultFetchListener) {
        currentPage = 1
        paginator = PublishProcessor.create()

        val d = paginator.onBackpressureDrop()
                .filter { !loading }
                .doOnNext {
                    if (currentPage != 1)
                        listener.showLoadingFooter()

                    loading = true
                }
                .concatMap {
                    localGithubResultsDataStore.getGitHubResults(repoName, currentPage, PAGE_ENTRIES)
                            .subscribeOn(subscribeScheduler)
                            .observeOn(observeScheduler)
                            .unsubscribeOn(subscribeScheduler)
                }
                .concatMap {
                    if (it.items.size <= 0 && NetworkHelper.isInternetOn)
                        remoteGithubResultsDataStore.getGitHubResults(repoName, currentPage, PAGE_ENTRIES)
                                .subscribeOn(subscribeScheduler)
                                .observeOn(observeScheduler)
                                .unsubscribeOn(subscribeScheduler)
                    else
                        Flowable.just(it)
                }
                .observeOn(observeScheduler, true)
                .subscribe({
                    if (currentPage == 0) {
                        // gitResultsView.hideLoading()
                    } else {
                        listener.hideLoadingFooter()
                    }
                    loading = false
                    if (it.items.size != 0) {
                        listener.onReposFetched(it.items)
                        currentPage++
                        if (it.items[0].from_cache == false)
                            saveLocalResults(repoName, it.items, listener)
                    } else {
                        currentPage = -1
                        loading = false
                        listener.hideLoading()
                    }
                }, {
                    listener.onReposError(it as Throwable)
                    loading = false
                })

        disposables?.add(d)

        fetchNextPage()

    }

    override fun destroyDisposable() {
        disposables?.clear()
    }

    override fun fetchNextPage() {
        if (currentPage >= 0)
            paginator.onNext(currentPage)
    }

    override fun getGitHubResultSubscribers(repoId: Int, repoName: String, listener: OnResultFetchListener) {

        currentPage = 1
        paginator = PublishProcessor.create()

        val d = paginator.onBackpressureDrop()
                .filter { !loading }
                .doOnNext {
                    if (currentPage != 1)
                        listener.showLoadingFooter()

                    loading = true
                }
                .flatMap {
                    localGithubResultsDataStore.getGitHubResultSubscribers(repoId, repoName, currentPage, PAGE_ENTRIES)
                            .subscribeOn(subscribeScheduler)
                            .observeOn(observeScheduler)
                            .unsubscribeOn(subscribeScheduler)

                }
                .flatMap {
                    if (it.size <= 0 && NetworkHelper.isInternetOn) {
                        remoteGithubResultsDataStore.getGitHubResultSubscribers(repoId, repoName, currentPage, PAGE_ENTRIES)
                                .subscribeOn(subscribeScheduler)
                                .observeOn(observeScheduler)
                                .unsubscribeOn(subscribeScheduler)
                    } else
                        Flowable.just(it)

                }
                .observeOn(observeScheduler, true)
                .subscribe({
                    if (currentPage != 1)
                        listener.hideLoadingFooter()

                    loading = false
                    if (it.size != 0) {
                        listener.onRepoSubscribersFetched(it)
                        currentPage++

                        if (it[0].parentRepo == "") {
                            saveLocalSubscribers(repoName, it, listener)
                        }
                    } else {
                        currentPage = -1
                        loading = false
                        listener.hideLoading()
                    }
                },
                        {
                            listener.onReposError(it as Throwable)
                            loading = false
                        })

        disposables?.add(d)

        fetchNextPage()

    }

    fun saveLocalResults(repoName: String, resultItems: ArrayList<RepoObject>, listener: OnResultFetchListener) {

        localGithubResultsDataStore.saveGitHubResultsDB(repoName, resultItems)
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
//                .unsubscribeOn(subscribeScheduler)
                .subscribe(object : SingleObserver<LongArray> {

                    override fun onSuccess(t: LongArray) {

                        var test: LongArray = t
                        test = t
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        listener.onReposError(e)
                    }
                })

    }


    fun saveLocalSubscribers(repoName: String, subscribers: ArrayList<OwnerObject>, listener: OnResultFetchListener) {

        localGithubResultsDataStore.saveGitHubResultSubscribersDB(repoName, subscribers)
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
//                .unsubscribeOn(subscribeScheduler)
                .subscribe(object : SingleObserver<LongArray> {

                    override fun onSuccess(t: LongArray) {
                        var test: LongArray = t
                        test = t
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        listener.onReposError(e)
                    }
                })

    }

}

