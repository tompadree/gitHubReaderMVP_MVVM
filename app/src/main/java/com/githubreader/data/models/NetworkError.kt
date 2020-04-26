package com.githubreader.data.models

/**
 * @author Tomislav Curis
 */
import com.google.gson.annotations.SerializedName

class NetworkError() {
    @SerializedName("error")
    var message: String? = null
    @SerializedName("type")
    var type: String? = null
}