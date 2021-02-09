package com.uni.data.dataSources.definitions

import com.uni.data.models.*
import okhttp3.RequestBody

abstract class DataSourceImage : BaseDataSource() {

    abstract suspend fun addRunsheet(requestBody: RequestBody, res: (SimpleResponse?) -> Unit)

    abstract suspend fun uploadCollected(requestBody: RequestBody, res: (SimpleResponse?) -> Unit)

    abstract suspend fun uploadDeposited(requestBody: RequestBody, res: (SimpleResponse?) -> Unit)

    abstract suspend fun uploadProfilePic(requestBody: RequestBody, res: (ProfilePicResponse?) -> Unit)
}