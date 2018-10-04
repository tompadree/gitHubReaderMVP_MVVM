package com.githubreader.view.adapters

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.githubreader.R
import com.githubreader.data.models.OwnerObject
import com.githubreader.utils.AppConstants
import com.squareup.picasso.Picasso

/**
 * Created by Tom on 22.5.2018..
 */
class GitResultDetailsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    lateinit var context: Context
    lateinit var subscribers: ArrayList<OwnerObject>
    private var isLoadingAdded: Boolean = false

    constructor()

    constructor(context: Context, subscribers: ArrayList<OwnerObject>) : super() {
        this.context = context
        this.subscribers = subscribers
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == AppConstants.ITEM) {
            ResultItemHolder(LayoutInflater.from(context).inflate(R.layout.item_git_result, parent, false))
        } else {
            LoadingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_git_result_loading, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        if (viewType == AppConstants.ITEM) {
            holder as ResultItemHolder

            val item = subscribers[position]

            holder.name.text = item.userName
            holder.name.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            holder.repoForksLabel.visibility = View.INVISIBLE

            Picasso.get()
                    .load(item.avatarUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .tag(context)
                    .fit().centerCrop()
                    .into(holder.repo_item_image_view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (subscribers[position].userType != null) {
            AppConstants.ITEM
        } else {
            AppConstants.LOADING
        }
    }

    override fun getItemCount(): Int {
        return subscribers.size
    }

    class ResultItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.repo_item_main_relative_layout)
        lateinit var repoItem: RelativeLayout

        @BindView(R.id.repo_item_image_view)
        lateinit var repo_item_image_view: ImageView

        @BindView(R.id.repo_forks_text_view_label)
        lateinit var repoForksLabel : TextView

        @BindView(R.id.repo_item_name_text_view)
        lateinit var name: TextView

        @BindView(R.id.repo_desc_id_text_view)
        lateinit var repoDesc: TextView

        @BindView(R.id.repo_forks_text_view)
        lateinit var repoForks: TextView

        init {
            ButterKnife.bind(this, itemView)
        }

    }

    protected inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun add(owner: OwnerObject) {
        subscribers.add(owner)
        notifyItemInserted(subscribers.size - 1)
    }

    fun addAll(subscribersListList: List<OwnerObject>) {
        for (sl in subscribersListList) {
            add(sl)
        }
    }

    fun remove(owner: OwnerObject) {
        val position = subscribers.indexOf(owner)
        if (position > -1) {
            subscribers.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(OwnerObject())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position = if (subscribers.size > 0) subscribers.size - 1 else 0
//        val item = getItem(position)

        subscribers.removeAt(position)
        notifyItemRemoved(position)

    }

    fun getItem(position: Int): OwnerObject {
        return subscribers[position]
    }

}