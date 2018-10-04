package com.githubreader.di

import com.githubreader.GitResultsActivityTest
import com.githubreader.di.component.AppComponent
import com.githubreader.di.module.AppModule
import com.githubreader.di.module.DataModule
import com.githubreader.di.module.NetModule
import com.githubreader.di.module.ThreadModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Tom on 24.5.2018..
 */
@Singleton
@Component(modules = [AppModule::class, NetModule::class, DataModule::class, ThreadModule::class])
interface AppComponentTest : AppComponent {

    fun inject(GitResultsActivityTest : GitResultsActivityTest)
}