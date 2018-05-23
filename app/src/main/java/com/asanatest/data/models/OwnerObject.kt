package com.asanatest.data.models;

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverter
import android.support.annotation.NonNull
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Serializable

/**
 * Created by Tomislav on 23,May,2018
 */
@Entity(tableName = "owners")
class OwnerObject : Serializable {

    @NonNull
    @PrimaryKey
    @SerializedName("login")
    var userName: String? = ""

    @SerializedName("avatar_url")
    var avatarUrl: String? = null

    @SerializedName("type")
    var userType: String? = null

    @SerializedName("site_admin")
    var siteAdmin: String? = null

    @SerializedName("parent_repo")
    var parentRepo : String? = ""
}