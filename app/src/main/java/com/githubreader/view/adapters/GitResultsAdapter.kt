package com.githubreader.view.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.githubreader.R
import com.githubreader.data.models.RepoObject
import com.githubreader.domain.listeners.OnResultItemClicked
import com.githubreader.utils.AppConstants.Companion.ITEM
import com.githubreader.utils.AppConstants.Companion.LOADING
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_git_result.*

/**
 * Created by Tom on 22.5.2018..
 */
class GitResultsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    lateinit var context: Context
    lateinit var repos: ArrayList<RepoObject>
    private var isLoadingAdded: Boolean = false
    private lateinit var onListItemClicked: OnResultItemClicked

    constructor()

    constructor(context: Context, repos: ArrayList<RepoObject>, onListItemClicked: OnResultItemClicked) : super() {
        this.context = context
        this.repos = repos
        this.onListItemClicked = onListItemClicked
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM) {
            ResultItemHolder(LayoutInflater.from(context).inflate(R.layout.item_git_result, parent, false))
        } else {
            LoadingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_git_result_loading, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var viewType = getItemViewType(position)

        if (viewType == ITEM) {
            holder as ResultItemHolder

            val item = repos[position]

            holder.repo_item_name_text_view.text = item.repoName
            holder.repo_desc_id_text_view.text = item.description
            holder.repo_forks_text_view.text = item.forks_count.toString()
            holder.repo_stars_text_view.text = item.stargazers_count.toString()

            holder.repo_item_main_relative_layout.setOnClickListener { onListItemClicked.onItemClicked(position) }

//            Picasso.get()
//                    .load(item.owner?.avatarUrl)
//                    .placeholder(R.mipmap.ic_launcher)
//                    .tag(context)
//                    .fit().centerCrop()
//                    .into(holder.repo_item_image_view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (repos[position].createdAt != null) {
            ITEM
        } else {
            LOADING
        }
    }

    override fun getItemCount(): Int {
        return repos.size
    }

    class ResultItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {

        override val containerView: View? = itemView

//        @BindView(R.id.repo_item_main_relative_layout)
//        lateinit var repoItem: RelativeLayout
//
//        @BindView(R.id.repo_item_image_view)
//        lateinit var repo_item_image_view: ImageView
//
//        @BindView(R.id.repo_item_name_text_view)
//        lateinit var name: TextView
//
//        @BindView(R.id.repo_desc_id_text_view)
//        lateinit var repoDesc: TextView
//
//        @BindView(R.id.repo_forks_text_view)
//        lateinit var repoForks: TextView
//
//        @BindView(R.id.repo_stars_text_view)
//        lateinit var repoStars: TextView
//
//        init {
//            ButterKnife.bind(this, itemView)
//        }

    }

    protected inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun add(repo: RepoObject) {
        repos.add(repo)
        notifyItemInserted(repos.size - 1)
    }

    fun addAll(repoList: List<RepoObject>) {
        for (rl in repoList) {
            add(rl)
        }
    }

    fun remove(repo: RepoObject) {
        val position = repos.indexOf(repo)
        if (position > -1) {
            repos.removeAt(position)
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
        add(RepoObject())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position = if (repos.size > 0) repos.size - 1 else 0

        repos.removeAt(position)
        notifyItemRemoved(position)

    }

    fun getItem(position: Int): RepoObject {
        return repos[position]
    }

}