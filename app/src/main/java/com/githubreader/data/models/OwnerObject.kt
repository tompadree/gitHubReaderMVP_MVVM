package com.githubreader.data.models;

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.annotation.NonNull
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Tomislav on 23,May,2018
 */
@Entity(tableName = "owners")
class OwnerObject constructor(

    @NonNull
    @PrimaryKey
    @SerializedName("login")
    var userName: String = "",

    @SerializedName("parent_repo")
    var parentRepo: String? = "",

    @SerializedName("avatar_url")
    var avatarUrl: String? = null,

    @SerializedName("type")
    var userType: String? = null,

    @SerializedName("site_admin")
    var siteAdmin: String? = null


) : Serializable

class OwnerConverter {

    @TypeConverter
    fun stringToLicense(person: String): OwnerObject? =
        Gson().fromJson(person, OwnerObject::class.java)

    @TypeConverter
    fun fromLicenseToString(person: OwnerObject): String? = Gson().toJson(person)

}
