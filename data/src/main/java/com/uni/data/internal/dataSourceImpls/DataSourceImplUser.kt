package com.uni.data.internal.dataSourceImpls

import android.util.Log
import com.uni.data.dataSources.definitions.DataSourceUser
import com.uni.data.models.SimpleResponse
import com.uni.data.models.User
import okhttp3.RequestBody
import org.json.JSONObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

internal class DataSourceImplUser : DataSourceUser() {

    override suspend fun loginUser(phoneNumber: String, password: String, res: (User?) -> Unit) {
        Log.e("USER IMPL::", "Enter")
        val json = JSONObject()
        json.put("username", phoneNumber)
        json.put("password", password)
        val requestBody: RequestBody =
                json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        res(apiRequest { API.userLogin(requestBody) })
    }

    override suspend fun editProfile(aadhar: String, pan: String, account: String, ifsc: String,
                                     email: String, alternate: String, res: (SimpleResponse?) -> Unit){
        val json = JSONObject()
        json.put("aadhar_number", aadhar)
        json.put("pan_card", pan)
        json.put("bank_account_no", account)
        json.put("ifsc_no", ifsc)
        json.put("branch_name", "")
        json.put("email", email)
        json.put("alternate_no", alternate)
        val requestBody: RequestBody =
                json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        res(apiRequest { API.editProfile(requestBody, repoPrefs.getLoggedInUser()?.token) })
    }

    override suspend fun sendOTP(phone: String?, res: (SimpleResponse?) -> Unit) {
        res(apiRequest { API.sendOtp(phone, repoPrefs.getLoggedInUser()?.token) })
    }

    override suspend fun verifyOTP(requestBody: RequestBody, res: (SimpleResponse?) -> Unit) {
        res(apiRequest { API.verifyOtp(requestBody, repoPrefs.getLoggedInUser()?.token) })
    }
}