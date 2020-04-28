package com.githubreader.gitresults

import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.githubreader.R
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject
import com.githubreader.data.source.remote.api.APIConstants
import com.githubreader.data.source.remote.api.APIConstants.Companion.DUMMY_SEARCH
import com.githubreader.gitresultsdetails.GitHubResultsDetailsAdapter

/**
 * @author Tomislav Curis
 */

// custom binding of items
@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<RepoObject>?) {
    if(items.isNullOrEmpty()) return

    (listView.adapter as GitHubResultsAdapter).submitList(items)
}

@BindingAdapter("app:subscribersItems")
fun setSubscriberItems(listViewSubscribers: RecyclerView, subscribersItems: List<OwnerObject>?) {
    if(subscribersItems.isNullOrEmpty()) return

    (listViewSubscribers.adapter as GitHubResultsDetailsAdapter).submitList(subscribersItems)
}

@BindingAdapter("imageSource")
fun setIcon(imageView: ImageView, avatarUrl: String?) {

    try {
        Glide.with(imageView.context).load(avatarUrl).into(imageView);
        imageView.clearFocus()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("imageSubscribersSource")
fun setIconSubscribers(imageViewSubs: ImageView, avatarUrlOwner: String?) {

    try {
        Glide.with(imageViewSubs.context).load(avatarUrlOwner).into(imageViewSubs);
        imageViewSubs.clearFocus()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("app:searchResult")
fun fetchSearchResults(gitResultToolbar: Toolbar, queryListener: OnSearchTermListener) {

    gitResultToolbar.inflateMenu(R.menu.git_results_menu)

    val searchItem = gitResultToolbar.menu.findItem(R.id.action_search)
    val searchView = searchItem.actionView as SearchView

    var lastText = ""

    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

        override fun onQueryTextChange(newText: String): Boolean {
                if (newText != lastText) {
                    if (newText == "")
                        queryListener.onQuery(APIConstants.DUMMY_SEARCH)
                    else
                        queryListener.onQuery(newText)
                    lastText = newText
                }
            return false
        }

        override fun onQueryTextSubmit(query: String): Boolean {
                if (!query.isEmpty()) {

                    if (query == "")
                        queryListener.onQuery(DUMMY_SEARCH)
                    else
                        queryListener.onQuery(query)
                }
            return false
        }
    })
}

interface OnSearchTermListener {
    fun onQuery(query: String)
}