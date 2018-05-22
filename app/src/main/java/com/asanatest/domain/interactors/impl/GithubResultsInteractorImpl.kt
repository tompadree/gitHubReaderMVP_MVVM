package com.asanatest.domain.interactors.impl

import android.content.Context
import com.asanatest.data.api.APIConstants.Companion.PAGE_ENTRIES
import com.asanatest.data.models.RepoObject
import com.asanatest.data.repositories.githubresults.LocalGithubResultsDataStore
import com.asanatest.data.repositories.githubresults.RemoteGithubResultsDataStore
import com.asanatest.di.module.ThreadModule
import com.asanatest.domain.interactors.GithubResultsInteractor
import com.asanatest.domain.listeners.OnResultFetchListener
import io.reactivex.Scheduler
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import javax.inject.Inject
import javax.inject.Named

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
        currentPage = 0 // set page = 1
//        var loading = false
        paginator = PublishProcessor.create()   // create PublishProcessor

        val d = paginator.onBackpressureDrop()
                .filter { !loading }  // return if it is still loading
                .doOnNext {
                    if (currentPage == 0) {
                        //  listener.showLoading()
                    } else {
                        listener.showLoadingFooter()
                    }
                    loading = true
                }
                .concatMap {
                    remoteGithubResultsDataStore.getGitHubResults(repoName, currentPage.toString(), PAGE_ENTRIES.toString())
                            .subscribeOn(subscribeScheduler)
                            .observeOn(observeScheduler)
                } // API call
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
                .concatMap {
                    remoteGithubResultsDataStore.getGitHubResultSubscribers(repoId, repoName, currentPage.toString(),
                            PAGE_ENTRIES.toString())
                            .subscribeOn(subscribeScheduler)
                            .observeOn(observeScheduler)
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
                        currentPage++
                    } else {
                        currentPage = -1
                    }
                }, {
                    listener.onReposError(it as Throwable)
                    loading = false
                })

        disposables?.add(d)

        fetchNextPage()


//        localGithubResultsDataStore.getGitHubResult(repoId, repoName)
//                .subscribeOn(subscribeScheduler)
//                .observeOn(observeScheduler)
//                .unsubscribeOn(subscribeScheduler)
//                .subscribe(object : SingleObserver<RepoObject> {
//
//                    override fun onSubscribe(d: Disposable) {
//                    }
//
//                    override fun onSuccess(t: RepoObject) {
//                        listener.onRepoFetched(t)
//
//                    }
//
//                    override fun onError(e: Throwable) {
//                        remoteGitHubResult(repoName, listener)
//                    }
//                })
    }

//    fun remoteGitHubResult(repoName: String, listener: OnResultFetchListener) {
//
//        remoteGithubResultsDataStore.getGitHubResult(0, repoName)
//                .subscribeOn(subscribeScheduler)
//                .observeOn(observeScheduler)
//                .unsubscribeOn(subscribeScheduler)
//                .subscribe(object : SingleObserver<RepoObject> {
//
//                    override fun onSubscribe(d: Disposable) {
//                    }
//
//                    override fun onSuccess(t: RepoObject) {
//                        listener.onRepoFetched(t)
//                    }
//
//                    override fun onError(e: Throwable) {
//                        listener.onReposError(e)
//                    }
//                })
//
//    }
}