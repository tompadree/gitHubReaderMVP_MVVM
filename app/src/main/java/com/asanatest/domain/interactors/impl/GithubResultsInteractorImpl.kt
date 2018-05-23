package com.asanatest.domain.interactors.impl

import android.content.Context
import com.asanatest.data.api.APIConstants.Companion.PAGE_ENTRIES
import com.asanatest.data.models.OwnerObject
import com.asanatest.data.models.RepoObject
import com.asanatest.data.repositories.githubresults.LocalGithubResultsDataStore
import com.asanatest.data.repositories.githubresults.RemoteGithubResultsDataStore
import com.asanatest.di.module.ThreadModule
import com.asanatest.domain.interactors.GithubResultsInteractor
import com.asanatest.domain.listeners.OnResultFetchListener
import io.reactivex.CompletableOnSubscribe
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.processors.PublishProcessor
import org.reactivestreams.Publisher
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.ArrayList

/**
 * Created by Tom on 22.5.2018..
 */
class GithubResultsInteractorImpl
@Inject constructor(private val context: Context, private val localGithubResultsDataStore: LocalGithubResultsDataStore,
                    private val remoteGithubResultsDataStore: RemoteGithubResultsDataStore) : GithubResultsInteractor {

    @Inject
    @field:Named(ThreadModule.SUBSCRIBE_SCHEDULER)
    lateinit var subscribeScheduler: Scheduler

    @Inject
    @field:Named(ThreadModule.OBSERVE_SCHEDULER)
    lateinit var observeScheduler: Scheduler

    private var currentPage: Int = 0
    private val disposables: CompositeDisposable? = null
    private lateinit var paginator: PublishProcessor<Int>
    private var loading: Boolean = false

    override fun getGitHubResults(repoName: String, listener: OnResultFetchListener) {
        currentPage = 1 // set page = 0
//        var loading = false
        paginator = PublishProcessor.create()   // create PublishProcessor

        val d = paginator.onBackpressureDrop()
                .filter { !loading }  // return if it is still loading
                .doOnNext {
                    if (currentPage == 1) {
                        //  listener.showLoading()
                    } else {
                        listener.showLoadingFooter()
                    }
                    loading = true
                }
//                .concatMap {
//                    localGithubResultsDataStore.getGitHubResults1(repoName, currentPage, PAGE_ENTRIES)
//                            .subscribeOn(subscribeScheduler)
//                            .observeOn(observeScheduler)
//                            .unsubscribeOn(subscribeScheduler)
//                }
//                .concatMap {
//                    localGithubResultsDataStore.getGitHubResults(repoName, currentPage, PAGE_ENTRIES)
//                            .subscribeOn(subscribeScheduler)
//                            .observeOn(observeScheduler)
//                            .unsubscribeOn(subscribeScheduler)
//                            .doOnError {
//                                remoteGithubResultsDataStore.getGitHubResults(repoName, currentPage, PAGE_ENTRIES)
//                                        .subscribeOn(subscribeScheduler)
//                                        .observeOn(observeScheduler)
//                                        .unsubscribeOn(subscribeScheduler)
//                            }
//                }
                .concatMap {
//                    if (it.items.size <= 0)
                        remoteGithubResultsDataStore.getGitHubResults(repoName, currentPage, PAGE_ENTRIES)
                                .subscribeOn(subscribeScheduler)
                                .observeOn(observeScheduler)
                                .unsubscribeOn(subscribeScheduler)
//                    else
//                        Flowable.just(it)
                }// API call
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
                        saveLocalResults(repoName, it.items, listener)
                    } else {
                        currentPage = -1
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

        currentPage = 1 // set page = 1
//        var loading = false
        paginator = PublishProcessor.create()   // create PublishProcessor

        val d = paginator.onBackpressureDrop()
                .filter { !loading }  // return if it is still loading
                .doOnNext {
                    if (currentPage == 1) {
                        //  listener.showLoading()
                    } else {
                        listener.showLoadingFooter()
                    }
                    loading = true
                }
                .flatMap {
                    localGithubResultsDataStore.getGitHubResultSubscribers(repoId, repoName, currentPage, PAGE_ENTRIES)
                            .subscribeOn(subscribeScheduler)
                            .observeOn(observeScheduler)
                            .unsubscribeOn(subscribeScheduler)

                }
                .flatMap {
                    if (it.size <= 0)
                        remoteGithubResultsDataStore.getGitHubResultSubscribers(repoId, repoName, currentPage, PAGE_ENTRIES)
                                .subscribeOn(subscribeScheduler)
                                .observeOn(observeScheduler)
                                .unsubscribeOn(subscribeScheduler)
                    else
                        Flowable.just(it)

                } // API call
                .observeOn(observeScheduler, true)
                .subscribe({
                    if (currentPage == 1) {
                        // gitResultsView.hideLoading()
                    } else {
                        listener.hideLoadingFooter()
                    }
                    loading = false
                    if (it.size != 0) {

                        listener.onRepoSubscribersFetched(it)
                        if (it[0].parentRepo == "") {
                            currentPage++
                            saveLocalSubscribers(repoName, it, listener)
                        }
                    } else {
                        currentPage = -1
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
                .unsubscribeOn(subscribeScheduler)
                .subscribe(object : SingleObserver<LongArray> {

                    override fun onSuccess(t: LongArray) {

                        var test: LongArray = t as LongArray

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
                .unsubscribeOn(subscribeScheduler)
                .subscribe(object : SingleObserver<LongArray> {

                    override fun onSuccess(t: LongArray) {

                        var test: LongArray = t as LongArray

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

