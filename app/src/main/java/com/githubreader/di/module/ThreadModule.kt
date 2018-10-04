package com.githubreader.di.module

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Tom on 22.5.2018..
 */
@Module
class ThreadModule {

    @Provides
    @Singleton
    @Named(OBSERVE_SCHEDULER)
    fun provideAndroidSchedulersMainThread(): Scheduler = AndroidSchedulers.mainThread()


    @Provides
    @Singleton
    @Named(SUBSCRIBE_SCHEDULER)
    fun provideSchedulersIo(): Scheduler = Schedulers.io()


    companion object {

        const val OBSERVE_SCHEDULER = "ObserveScheduler"

        const val SUBSCRIBE_SCHEDULER = "SubscribeScheduler"
    }
}