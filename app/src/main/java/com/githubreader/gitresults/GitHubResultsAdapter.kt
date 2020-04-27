package com.githubreader.gitresults

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.githubreader.data.models.RepoObject
import com.githubreader.databinding.ItemGitResultBinding
import com.githubreader.gitresults.diffUtil.ReposDiffUtil
import kotlinx.android.extensions.LayoutContainer

/**
 * @author Tomislav Curis
 */

class GitHubResultsAdapter(private val gitResultsViewModel: GitResultsViewModel)
    : ListAdapter<RepoObject, ReposViewHolder>(ReposDiffUtil()) {

    override fun getItemId(position: Int): Long = getItem(position).repoName.hashCode().toLong()
    override fun getItemViewType(position: Int) = position

    override fun onBindViewHolder(holder: ReposViewHolder, position: Int) {

        holder.bind(gitResultsViewModel, getItem(position), position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposViewHolder {
        return ReposViewHolder.from(parent)
    }

    override fun submitList(list: List<RepoObject>?) {
        super.submitList(list)
    }
}

class ReposViewHolder private constructor(val binding: ItemGitResultBinding)
    : RecyclerView.ViewHolder(binding.root), LayoutContainer {

    override val containerView = binding.root

    companion object {
        fun from(parent: ViewGroup): ReposViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemGitResultBinding.inflate(layoutInflater, parent, false)

            return ReposViewHolder(binding)
        }
    }

    fun bind(gitResultsViewModel: GitResultsViewModel, item: RepoObject, position: Int) {
        binding.viewModel = gitResultsViewModel
        binding.repoObject = item
        binding.executePendingBindings()
    }

}