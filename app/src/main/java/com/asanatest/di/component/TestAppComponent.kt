package com.asanatest.di.component

import android.content.Context
import com.asanatest.data.api.NetworkApi
import com.asanatest.data.db.GitHubDatabase
import com.asanatest.di.module.AppModule
import com.asanatest.di.module.DataModule
import com.asanatest.di.module.NetModule
import com.asanatest.di.module.ThreadModule
import com.asanatest.view.activities.BaseActivity
import dagger.Component
import io.reactivex.Scheduler
import javax.inject.Singleton


/**
 * Created by Tomislav on 24,May,2018
 */
@Singleton
@Component(modules = [AppModule::class, NetModule::class, DataModule::class, ThreadModule::class])
interface TestAppComponent : AppComponent {

    override fun context(): Context

    override fun networkApi(): NetworkApi

    override fun gitHubDatabase(): GitHubDatabase

//    override fun provideAndroidSchedulersMainThread(): Scheduler
//
//    override fun provideSchedulersIo(): Scheduler

    override fun inject(baseActivity: BaseActivity)
}