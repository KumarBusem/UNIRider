package com.dcartlogistics.data.dataSources.definitions

import com.dcartlogistics.data.models.SimpleResponse
import com.dcartlogistics.data.models.User
import okhttp3.RequestBody

abstract class DataSourceUser : BaseDataSource() {

    abstract suspend fun loginUser(phoneNumber: String, password: String, res: (User?) -> Unit)
    abstract suspend fun editProfile(aadhar: String, pan: String, account: String, ifsc: String,
                                     email: String, alternate: String, res: (SimpleResponse?) -> Unit)

    abstract suspend fun sendOTP(phone: String?, res: (SimpleResponse?) -> Unit)

    abstract suspend fun verifyOTP(requestBody: RequestBody, res: (SimpleResponse?) -> Unit)

}