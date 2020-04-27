package com.githubreader.gitresultsdetails

import androidx.recyclerview.widget.DiffUtil
import com.githubreader.data.models.OwnerObject

/**
 * @author Tomislav Curis
 */
class OwnersDiffUtil : DiffUtil.ItemCallback<OwnerObject>() {
    override fun areContentsTheSame(oldItem: OwnerObject, newItem: OwnerObject): Boolean {
        return oldItem.avatarUrl == newItem.avatarUrl
    }

    override fun areItemsTheSame(oldItem: OwnerObject, newItem: OwnerObject): Boolean {
        return oldItem.userName == newItem.userName
    }
}