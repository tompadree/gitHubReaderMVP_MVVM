package com.asanatest.di.component

import android.content.Context
import com.asanatest.data.api.NetworkApi
import com.asanatest.data.db.GitHubCache
import com.asanatest.data.db.GitHubDatabase
import com.asanatest.di.module.AppModule
import com.asanatest.di.module.DataModule
import com.asanatest.di.module.NetModule
import com.asanatest.di.module.ThreadModule
import com.asanatest.di.module.ThreadModule.Companion.OBSERVE_SCHEDULER
import com.asanatest.di.module.ThreadModule.Companion.SUBSCRIBE_SCHEDULER
import com.asanatest.view.activities.BaseActivity
import dagger.Component
import io.reactivex.Scheduler
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Tom on 22.5.2018..
 */
@Singleton
@Component(modules = [AppModule::class, NetModule::class, DataModule::class, ThreadModule::class])
interface AppComponent {

    fun context(): Context

    fun networkApi(): NetworkApi

    fun gitHubDatabase(): GitHubDatabase

    fun gitHubCache(): GitHubCache

    @Named(OBSERVE_SCHEDULER)
    fun provideAndroidSchedulersMainThread(): Scheduler

    @Named(SUBSCRIBE_SCHEDULER)
    fun provideSchedulersIo(): Scheduler

    fun inject(baseActivity: BaseActivity)

}