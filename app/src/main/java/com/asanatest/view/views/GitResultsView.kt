package com.asanatest.view.views

import com.asanatest.data.models.RepoObject

/**
 * Created by Tom on 22.5.2018..
 */
interface GitResultsView : DefaultView {

    fun updateList(repos : ArrayList<RepoObject>)

    fun repoSubscribersFetched(subscribers : ArrayList<RepoObject.Owner>)

    fun showLoadingFooter()

    fun hideLoadingFooter()

    fun showRefreshLoading()

    fun hideRefreshLoading()
}