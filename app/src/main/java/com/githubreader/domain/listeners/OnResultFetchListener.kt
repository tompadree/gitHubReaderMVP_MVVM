package com.githubreader.domain.listeners

import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject

/**
 * Created by Tom on 22.5.2018..
 */
interface OnResultFetchListener {

    fun onReposFetched(repos : ArrayList<RepoObject>)

    fun onRepoSubscribersFetched(subscribers : ArrayList<OwnerObject>)

    fun onReposError(e : Throwable)

    fun showLoadingFooter()

    fun hideLoadingFooter()

    fun showLoading()

    fun hideLoading()

}