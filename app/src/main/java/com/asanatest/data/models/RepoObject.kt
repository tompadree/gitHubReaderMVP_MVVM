package com.asanatest.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters
import android.support.annotation.NonNull
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.security.acl.Owner

/**
 * Created by Tom on 22.5.2018..
 */

@Entity(tableName = "repos")
class RepoObject : Serializable {

    @SerializedName("id")
    var repoId: Int = 0

    @NonNull
    @PrimaryKey
    @SerializedName("full_name")
    var repoName: String? = ""

    @SerializedName("watchers_count")
    var watchers_count: Int = 0

    @SerializedName("forks_count")
    var forks_count: Int = 0

    @SerializedName("subscribers_count")
    var subscribers_count: Int = 0

    @SerializedName("open_issues_count")
    var open_issues_count: Int = 0

    @SerializedName("description")
    var description: String? = ""

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("updated_at")
    var modified: String? = null

    @SerializedName("language")
    var language: String? = null

    @SerializedName("owner")
    @TypeConverters(OwnerConverter::class)
    var owner: Owner? = Owner()

    class Owner : Serializable {

        @SerializedName("login")
        var userName: String? = ""

        @SerializedName("avatar_url")
        var avatarUrl: String? = null

        @SerializedName("type")
        var userType: String? = null

        @SerializedName("site_admin")
        var siteAdmin: String? = null

        @SerializedName("parent_repo")
        var parentRepo: String? = ""

    }

    class OwnerConverter {

        @TypeConverter
        fun stringToOwner(value: String): Owner {
            val listType = object : TypeToken<ArrayList<String>>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        fun fromOwnerToString(owner: Owner): String = Gson().toJson(owner)

    }
}