package com.asanatest.data.models

import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Serializable

/**
 * Created by Tom on 22.5.2018..
 */
class ReposModel : Serializable {

    @SerializedName("total_count")
    var total_count: Int = 0

    @SerializedName("incomplete_results")
    var incomplete_results: Boolean = false

    @TypeConverters(ReposConverter::class)
    @SerializedName("items")
    var items: ArrayList<RepoObject> = ArrayList()

    class ReposConverter {

        @TypeConverter
        fun stringToRepos(value: String): ArrayList<RepoObject> {

            val listType = object : TypeToken<ArrayList<String>>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        fun fromReposToString(list: ArrayList<RepoObject>): String = Gson().toJson(list)

    }
}