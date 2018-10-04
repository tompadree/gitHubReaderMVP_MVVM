package com.githubreader.di.component

import android.content.Context
import com.githubreader.data.api.NetworkApi
import com.githubreader.data.db.GitHubDatabase
import com.githubreader.di.module.AppModule
import com.githubreader.di.module.DataModule
import com.githubreader.di.module.NetModule
import com.githubreader.di.module.ThreadModule
import com.githubreader.di.module.ThreadModule.Companion.OBSERVE_SCHEDULER
import com.githubreader.di.module.ThreadModule.Companion.SUBSCRIBE_SCHEDULER
import com.githubreader.view.activities.BaseActivity
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

    @Named(OBSERVE_SCHEDULER)
    fun provideAndroidSchedulersMainThread(): Scheduler

    @Named(SUBSCRIBE_SCHEDULER)
    fun provideSchedulersIo(): Scheduler

    fun inject(baseActivity: BaseActivity)

}