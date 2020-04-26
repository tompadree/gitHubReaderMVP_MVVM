package com.githubreader.data.models

/**
 * @author Tomislav Curis
 */
import com.google.gson.annotations.SerializedName

class NetworkError() {
    @SerializedName("message")
    var message: String = ""
    @SerializedName("documentation_url")
    var documentation_url: String = ""
    @SerializedName("errors")
    var errors: ArrayList<ErrorObject>? = ArrayList()
}

data class ErrorObject(
    @SerializedName("resource")
    var resource: String = "",
    @SerializedName("field")
    var field: String = "",
    @SerializedName("code")
    var code: String = ""

)

//{"message":"Validation Failed","errors":[{"resource":"Search","field":"q","code":"missing"}],"documentation_url":"https://developer.github.com/v3/search"}
