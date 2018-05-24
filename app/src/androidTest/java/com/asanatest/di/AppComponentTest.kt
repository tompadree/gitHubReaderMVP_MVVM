package com.asanatest.di

import com.asanatest.GitResultsActivityTest
import com.asanatest.di.component.AppComponent
import com.asanatest.di.module.AppModule
import com.asanatest.di.module.DataModule
import com.asanatest.di.module.NetModule
import com.asanatest.di.module.ThreadModule
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