package com.asanatest.di.component

import com.asanatest.di.ResultScope
import com.asanatest.di.module.GitResultModule
import com.asanatest.view.activities.GitResults.GitResultDetailsActivity
import com.asanatest.view.activities.GitResults.GitResultsActivity
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