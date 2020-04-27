package com.githubreader.gitresults

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.githubreader.R
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject
import com.githubreader.gitresultsdetails.GitHubResultsDetailsAdapter
import kotlinx.android.synthetic.main.item_git_result.*

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