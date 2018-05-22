package com.asanatest.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Tom on 22.5.2018..
 */
class ReposModel : Serializable {

    @SerializedName("total_count")
    var total_count : Int = 0

    @SerializedName("incomplete_results")
    var incomplete_results : Boolean = false

    @SerializedName("items")
    var items : ArrayList<RepoObject> = ArrayList()

}