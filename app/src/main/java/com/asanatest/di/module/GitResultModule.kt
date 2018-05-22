package com.asanatest.di.module

import com.asanatest.data.repositories.githubresults.GitHubResultsDataStore
import com.asanatest.data.repositories.githubresults.LocalGithubResultsDataStore
import com.asanatest.data.repositories.githubresults.RemoteGithubResultsDataStore
import com.asanatest.di.ResultScope
import com.asanatest.domain.interactors.GithubResultsInteractor
import com.asanatest.domain.interactors.impl.GithubResultsInteractorImpl
import com.asanatest.presenter.GithubResultsPresenter
import com.asanatest.presenter.impl.GithubResultsPresenterImpl
import com.asanatest.view.views.GitResultsView
import dagger.Module
import dagger.Provides

/**
 * Created by Tom on 22.5.2018..
 */

@Module
class GitResultModule(private val gitResultsView: GitResultsView) {


    @Provides
    @ResultScope
    fun providesGitResultsView(): GitResultsView = this.gitResultsView

    @Provides
    @ResultScope
    fun providesGithubResultsPresenter(presenter: GithubResultsPresenterImpl): GithubResultsPresenter = presenter

    @Provides
    @ResultScope
    fun providesGithubResultsInteractor(interactor: GithubResultsInteractorImpl): GithubResultsInteractor = interactor

    @ResultScope
    @Provides
    fun providesRemoteGithubResultsDataStore(remoteObjectDataStore: RemoteGithubResultsDataStore): GitHubResultsDataStore = remoteObjectDataStore


    @ResultScope
    @Provides
    fun providesLocalGithubResultsDataStore(localObjectDataStore: LocalGithubResultsDataStore): GitHubResultsDataStore = localObjectDataStore
}