package com.githubreader.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import androidx.annotation.NonNull
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Tom on 22.5.2018..
 */

@Entity(tableName = "repos")
data class RepoObject constructor (

    @SerializedName("id")
    var repoId: Int = 0,

    @NonNull
    @PrimaryKey
    @SerializedName("full_name")
    var repoName: String = "",

    @SerializedName("watchers_count")
    var watchers_count: Int = 0,

    @SerializedName("forks_count")
    var forks_count: Int = 0,

    @SerializedName("subscribers_count")
    var subscribers_count: Int = 0,

    @SerializedName("open_issues_count")
    var open_issues_count: Int = 0,

    @SerializedName("stargazers_count")
    var stargazers_count: Int = 0,

    @SerializedName("description")
    var description: String? = "",

    @SerializedName("created_at")
    var createdAt: String? = null,

    @SerializedName("updated_at")
    var modified: String? = null,

    @SerializedName("from_cache")
    var from_cache: Boolean? = false,

    @SerializedName("language")
    var language: String? = null,

    @SerializedName("owner")
    @TypeConverters(OwnerConverter::class)
    var owner: OwnerObject? = OwnerObject()

) : Serializable
