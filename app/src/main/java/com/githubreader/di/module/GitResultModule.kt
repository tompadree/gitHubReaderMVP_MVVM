package com.githubreader.di.module

import com.githubreader.data.repositories.githubresults.GitHubResultsDataStore
import com.githubreader.data.repositories.githubresults.LocalGithubResultsDataStore
import com.githubreader.data.repositories.githubresults.RemoteGithubResultsDataStore
import com.githubreader.di.ResultScope
import com.githubreader.domain.interactors.GithubResultsInteractor
import com.githubreader.domain.interactors.impl.GithubResultsInteractorImpl
import com.githubreader.presenter.GithubResultsPresenter
import com.githubreader.presenter.impl.GithubResultsPresenterImpl
import com.githubreader.view.views.GitResultsView
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