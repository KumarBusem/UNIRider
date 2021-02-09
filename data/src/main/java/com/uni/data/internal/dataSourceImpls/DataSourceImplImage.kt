package com.uni.data.internal.dataSourceImpls

import com.uni.data.dataSources.definitions.DataSourceImage
import com.uni.data.models.*
import okhttp3.RequestBody

internal class DataSourceImplImage : DataSourceImage() {

    override suspend fun addRunsheet(requestBody: RequestBody, res: (SimpleResponse?) -> Unit) {
        res(imageUploadAPIRequest(API.uploadDailyRunsheet(requestBody, repoPrefs.getLoggedInUser()?.token)))
    }

    override suspend fun uploadCollected(requestBody: RequestBody, res: (SimpleResponse?) -> Unit) {
        res(imageUploadAPIRequest(API.uploadCollected(requestBody, repoPrefs.getLoggedInUser()?.token)))
    }

    override suspend fun uploadDeposited(requestBody: RequestBody, res: (SimpleResponse?) -> Unit) {
        res(imageUploadAPIRequest(API.uploadDeposit(requestBody, repoPrefs.getLoggedInUser()?.token)))
    }

    override suspend fun uploadProfilePic(requestBody: RequestBody, res: (ProfilePicResponse?) -> Unit){
        res(imageUploadAPIRequest(API.uploadProfilePic(requestBody, repoPrefs.getLoggedInUser()?.token)))
    }
}