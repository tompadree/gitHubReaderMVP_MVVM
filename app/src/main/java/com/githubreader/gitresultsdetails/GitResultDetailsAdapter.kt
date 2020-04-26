package com.githubreader.gitresultsdetails

import android.content.Context
import android.graphics.Typeface
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import butterknife.BindView
//import butterknife.ButterKnife
import com.githubreader.R
import com.githubreader.data.models.OwnerObject
import com.githubreader.data.models.RepoObject
import com.githubreader.utils.AppConstants
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_git_result.*
import kotlinx.android.synthetic.main.layout_repo_detail_header.*
import kotlinx.android.synthetic.main.layout_repo_details.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Tom on 22.5.2018..
 */
class GitResultDetailsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    lateinit var context: Context
    lateinit var subscribers: ArrayList<OwnerObject>
    private var isLoadingAdded: Boolean = false
    private val TYPE_DETAILS: Int = 2
    private val TYPE_LIST_HEADER: Int = 3
    lateinit var repoObject : RepoObject

    constructor()

    constructor(context: Context, subscribers: ArrayList<OwnerObject>, repoObject : RepoObject) : super() {
        this.context = context
        this.subscribers = subscribers
        this.repoObject = repoObject
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_DETAILS)
            DetailsHolder(
                LayoutInflater.from(context).inflate(R.layout.layout_repo_details, parent, false)
            )
        else if (viewType == TYPE_LIST_HEADER)
            ResultsHeaderHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.layout_repo_detail_header, parent, false)
            )
        else if (viewType == AppConstants.ITEM) {
            ResultItemHolder(
                LayoutInflater.from(context).inflate(R.layout.item_git_result, parent, false)
            )
        } else {
            LoadingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_git_result_loading, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        if (viewType == AppConstants.ITEM) {
            holder as ResultItemHolder

            val item = subscribers[position]

            holder.repo_item_name_text_view.text = item.userName
            holder.repo_item_name_text_view.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            holder.repo_forks_text_view_label.visibility = View.GONE
            holder.repo_forks_text_view_label.visibility = View.GONE

//            Picasso.get()
//                    .load(item.avatarUrl)
//                    .placeholder(R.mipmap.ic_launcher)
//                    .tag(context)
//                    .fit().centerCrop()
//                    .into(holder.repo_item_image_view)
        }

        else if (viewType == TYPE_DETAILS){
            holder as DetailsHolder
            val fmt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            val outputTime = SimpleDateFormat("H:mm  M.d.yyyy", Locale.US)

            if (repoObject.language != null)
                holder.layout_repo_details_tv_language.text = repoObject.language
            else
                holder.layout_repo_details_language_layout.visibility = View.GONE

            if (repoObject.createdAt != null)
                holder.layout_repo_details_tv_created.text = outputTime.format(fmt.parse(repoObject.createdAt))
            else
                holder.layout_repo_details_created_layout.visibility = View.GONE

            if (repoObject.modified != null)
                holder.layout_repo_details_tv_modified.text = outputTime.format(fmt.parse(repoObject.modified))
            else
                holder.layout_repo_details_modified_layout.visibility = View.GONE

            holder.layout_repo_details_tv_watchers.text = repoObject.watchers_count.toString()

            holder.layout_repo_details_tv_forks.text = repoObject.forks_count.toString()

            holder.layout_repo_details_tv_subscriptions.text = repoObject.subscribers_count.toString()

            if (repoObject.owner!!.userType != null)
                holder.layout_repo_details_tv_typeUser.text = repoObject.owner!!.userType
            else
                holder.layout_repo_details_typeUser_layout.visibility = View.GONE

            if (repoObject.owner!!.siteAdmin != null)
                holder.layout_repo_details_tv_siteAdmin.text = repoObject.owner!!.siteAdmin
            else
                holder.layout_repo_details_siteAdmin_layout.visibility = View.GONE

            holder.layout_repo_details_tv_issues.text = repoObject.open_issues_count.toString()

        } else if(viewType == TYPE_LIST_HEADER) {
            holder as ResultsHeaderHolder

            holder.repo_detail_info_layout.visibility = View.VISIBLE

            holder.repo_detail_name_text_view.text = repoObject.repoName

        }
    }

    override fun getItemViewType(position: Int): Int {

        return if (position == 0)
            TYPE_DETAILS
        else if (position == 1)
            TYPE_LIST_HEADER
        else if (subscribers[position].userType != null)
            AppConstants.ITEM
        else {
            AppConstants.LOADING
        }
    }

    override fun getItemCount(): Int {
        return subscribers.size
    }

    class ResultItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {

        override val containerView: View? = itemView

//        @BindView(R.id.repo_item_main_relative_layout)
//        lateinit var repoItem: RelativeLayout
//
//        @BindView(R.id.repo_item_image_view)
//        lateinit var repo_item_image_view: ImageView
//
//        @BindView(R.id.repo_forks_text_view_label)
//        lateinit var repoForksLabel: TextView
//
//        @BindView(R.id.repo_stars_text_view_label)
//        lateinit var repoStarsLabel: TextView
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
//        init {
//            ButterKnife.bind(this, itemView)
//        }

    }

    class DetailsHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer  {

        override val containerView: View? = itemView

//        @BindView(R.id.layout_repo_details_language_layout)
//        lateinit var layout_repo_details_language_layout: LinearLayout
//
//        @BindView(R.id.layout_repo_details_tv_language)
//        lateinit var layout_repo_details_tv_language: TextView
//
//        @BindView(R.id.layout_repo_details_created_layout)
//        lateinit var layout_repo_details_created_layout: LinearLayout
//
//        @BindView(R.id.layout_repo_details_tv_created)
//        lateinit var layout_repo_details_tv_created: TextView
//
//        @BindView(R.id.layout_repo_details_tv_modified)
//        lateinit var layout_repo_details_tv_modified: TextView
//
//        @BindView(R.id.layout_repo_details_modified_layout)
//        lateinit var layout_repo_details_modified_layout: LinearLayout
//
//        @BindView(R.id.layout_repo_details_tv_watchers)
//        lateinit var layout_repo_details_tv_watchers: TextView
//
//        @BindView(R.id.layout_repo_details_tv_forks)
//        lateinit var layout_repo_details_tv_forks: TextView
//
//        @BindView(R.id.layout_repo_details_tv_subscriptions)
//        lateinit var layout_repo_details_tv_subscriptions: TextView
//
//        @BindView(R.id.layout_repo_details_tv_typeUser)
//        lateinit var layout_repo_details_tv_typeUser: TextView
//
//        @BindView(R.id.layout_repo_details_typeUser_layout)
//        lateinit var layout_repo_details_typeUser_layout: LinearLayout
//
//        @BindView(R.id.layout_repo_details_tv_siteAdmin)
//        lateinit var layout_repo_details_tv_siteAdmin: TextView
//
//        @BindView(R.id.layout_repo_details_siteAdmin_layout)
//        lateinit var layout_repo_details_siteAdmin_layout: LinearLayout
//
//        @BindView(R.id.layout_repo_details_tv_issues)
//        lateinit var layout_repo_details_tv_issues: TextView
//
//        init {
//            ButterKnife.bind(this, itemView)
//        }

    }

    class ResultsHeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {

        override val containerView: View? = itemView

//        @BindView(R.id.repo_detail_info_layout)
//        lateinit var repo_detail_info_layout: RelativeLayout
//
//        @BindView(R.id.repo_detail_name_text_view)
//        lateinit var repo_detail_name_text_view: TextView
//
//        init {
//            ButterKnife.bind(this, itemView)
//        }

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