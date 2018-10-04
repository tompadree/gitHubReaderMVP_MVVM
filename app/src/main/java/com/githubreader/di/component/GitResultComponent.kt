package com.githubreader.di.component

import com.githubreader.di.ResultScope
import com.githubreader.di.module.GitResultModule
import com.githubreader.view.activities.GitResults.GitResultDetailsActivity
import com.githubreader.view.activities.GitResults.GitResultsActivity
import dagger.Component

/**
 * Created by Tom on 22.5.2018..
 */
@ResultScope
@Component(dependencies = [AppComponent::class], modules = [GitResultModule::class])
interface GitResultComponent {

    fun inject(gitResultsActivity: GitResultsActivity)

    fun inject(gitResultDetailsActivity: GitResultDetailsActivity)
}