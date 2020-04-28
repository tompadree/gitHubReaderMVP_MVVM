package com.githubreader.gitresultsdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.githubreader.data.models.OwnerObject
import com.githubreader.databinding.ItemGitResultDetailsBinding
import kotlinx.android.extensions.LayoutContainer

/**
 * @author Tomislav Curis
 */
class GitHubResultsDetailsAdapter (private val gitResultsDetailsViewModel: GitResultsDetailsViewModel)
    : ListAdapter<OwnerObject, SubscribersViewHolder>(OwnersDiffUtil()) {

    override fun getItemId(position: Int): Long = getItem(position).userName.hashCode().toLong()
    override fun getItemViewType(position: Int) = position

    override fun onBindViewHolder(holder: SubscribersViewHolder, position: Int) {

        holder.bind(gitResultsDetailsViewModel, getItem(position), position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscribersViewHolder {
        return SubscribersViewHolder.from(parent)
    }
}

class SubscribersViewHolder private constructor(val binding: ItemGitResultDetailsBinding)
    : RecyclerView.ViewHolder(binding.root), LayoutContainer {

    override val containerView = binding.root

    companion object {
        fun from(parent: ViewGroup): SubscribersViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemGitResultDetailsBinding.inflate(layoutInflater, parent, false)

            return SubscribersViewHolder(binding)
        }
    }

    fun bind(gitResultsDetailsViewModel: GitResultsDetailsViewModel, item: OwnerObject, position: Int) {
        binding.viewModel = gitResultsDetailsViewModel
        binding.ownerObject = item
        binding.executePendingBindings()
    }

}
