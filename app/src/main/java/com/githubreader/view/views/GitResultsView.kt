package com.githubreader.view.views

import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject

/**
 * Created by Tom on 22.5.2018..
 */
interface GitResultsView : DefaultView {

    fun updateList(repos : ArrayList<RepoObject>)

    fun repoSubscribersFetched(subscribers : ArrayList<OwnerObject>)

    fun showLoadingFooter()

    fun hideLoadingFooter()

    fun showRefreshLoading()

    fun hideRefreshLoading()
}