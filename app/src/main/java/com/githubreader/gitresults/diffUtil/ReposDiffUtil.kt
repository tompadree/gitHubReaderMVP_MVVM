package com.githubreader.gitresults.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.githubreader.data.models.RepoObject

/**
 * @author Tomislav Curis
 */
class ReposDiffUtil : DiffUtil.ItemCallback<RepoObject>() {
    override fun areContentsTheSame(oldItem: RepoObject, newItem: RepoObject): Boolean {
        return oldItem.modified == newItem.modified
    }

    override fun areItemsTheSame(oldItem: RepoObject, newItem: RepoObject): Boolean {
            return oldItem.repoId == newItem.repoId
    }
}