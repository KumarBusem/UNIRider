package com.uni.data.internal.common

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

abstract class SafeApiRequest {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T? {
        val response = call.invoke()

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            checkError(response.errorBody()?.string(), response.code(), response.message())
        }

        return null
    }
    suspend fun <T : Any> imageUploadAPIRequest(call: Call<T>): T? {

        val response = call.execute()

        if (response.isSuccessful)
            return response.body()
        else {
            checkError(response.errorBody()?.string(), response.code(), response.message())
        }
        return null
    }

    private fun checkError(error: String?, errorCode: Int, errorMessage: String) {
        if (errorCode == 401) {
            throw RiderLoginException()
        }
        val message = StringBuilder()
        error?.let {
            try {
                message.append(JSONObject(it).getString("message"))
            } catch (e: JSONException) {
            }
            try {
                message.append(JSONObject(it).getString("error"))
            } catch (e: JSONException) {
            }
            message.append(": $errorMessage : $errorCode")
        }
        throw ApiException(message.toString())
    }

}