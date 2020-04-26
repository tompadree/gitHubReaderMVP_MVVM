package com.githubreader.data.models

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Serializable

/**
 * Created by Tom on 22.5.2018..
 */
data class ReposModel (

    @SerializedName("total_count")
    var total_count: Int = 0,

    @SerializedName("incomplete_results")
    var incomplete_results: Boolean = false,

    @SerializedName("items")
    @TypeConverters(ReposConverter::class)
    var items: ArrayList<RepoObject> = ArrayList()


) : Serializable

class ReposConverter {

    companion object {

        @TypeConverter
        @JvmStatic
        fun stringToRepos(value: String): ArrayList<RepoObject> {

            val listType = object : TypeToken<ArrayList<String>>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        @JvmStatic
        fun fromReposToString(list: ArrayList<RepoObject>): String = Gson().toJson(list)

    }

}