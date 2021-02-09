package com.uni.data.dataSources.repos

import com.uni.data.dataSources.definitions.DataSourceUser
import com.uni.data.internal.dataSourceImpls.DataSourceImplUser
import com.uni.data.models.SimpleResponse
import com.uni.data.models.User
import okhttp3.RequestBody

class RepoUser : DataSourceUser() {

    private val mUserDataSource: DataSourceUser by lazy { DataSourceImplUser() }

    override suspend fun loginUser(phoneNumber: String, password: String, res: (User?) -> Unit) =
            mUserDataSource.loginUser(phoneNumber, password, res)


    override suspend fun editProfile(aadhar: String, pan: String, account: String, ifsc: String,
                                     email: String, alternate: String, res: (SimpleResponse?) -> Unit) =
            mUserDataSource.editProfile(aadhar, pan, account, ifsc, email, alternate, res)


    override suspend fun sendOTP(phone: String?, res: (SimpleResponse?) -> Unit)
            = mUserDataSource.sendOTP(phone, res)

    override suspend fun verifyOTP(requestBody: RequestBody, res: (SimpleResponse?) -> Unit)
            = mUserDataSource.verifyOTP(requestBody, res)
}