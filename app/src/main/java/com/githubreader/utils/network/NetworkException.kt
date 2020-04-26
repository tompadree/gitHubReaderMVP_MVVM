package com.githubreader.utils.network

import com.githubreader.data.models.NetworkError
import com.google.gson.Gson
import okhttp3.Response
import java.io.IOException

class NetworkException(response: Response?) : IOException() {

    private val localResponse: Response? = response
    private var error: NetworkError? = localResponse?.let { handleResponse(localResponse) }
    override var message: String = localResponse?.let { getMessage(localResponse) } ?: "Some error"


    private fun handleResponse(response: Response): NetworkError? {
        return try {
            val errorBodyJson = response.body()?.string() ?: "{}"

            val networkError = Gson().fromJson(errorBodyJson, NetworkError::class.java)
            message = networkError.message + ": " + networkError.errors?.get(0)?.resource + " "+
                    networkError.errors?.get(0)?.field + " " + networkError.errors?.get(0)?.code
            networkError
        } catch (e: Exception) {
            Gson().fromJson(e.localizedMessage, NetworkError::class.java)
        }
    }

    private fun getMessage(response: Response): String? {
        return try {
            if (message != "Some error")
                return message
            val errorBodyJson = response.body()?.string() ?: "{}"
            Gson().fromJson(errorBodyJson, NetworkError::class.java).message
        } catch (e: Exception) {
            e.localizedMessage
        }
    }

    fun getErrors(): NetworkError? {
        return error
    }
}