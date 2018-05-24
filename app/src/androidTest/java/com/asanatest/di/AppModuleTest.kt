package com.asanatest.di

import android.app.Application
import android.content.Context
import com.asanatest.domain.interactors.GithubResultsInteractor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Tom on 24.5.2018..
 */

@Module
class AppModuleTest (private val mApplication: Application) {

//    @Provides
//    @ResultScope
//    fun providesGithubResultsInteractor(interactor: GithubResultsInteractorTest): GithubResultsInteractor = interactor
//

}