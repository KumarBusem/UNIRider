package com.uni.data.dataSources.repos

import com.uni.data.dataSources.definitions.DataSourceImage
import com.uni.data.internal.dataSourceImpls.DataSourceImplImage
import com.uni.data.models.*
import okhttp3.RequestBody

class RepoImage : DataSourceImage() {

    private val mImageDataSource: DataSourceImage by lazy { DataSourceImplImage() }

    override suspend fun addRunsheet(requestBody: RequestBody, res: (SimpleResponse?) -> Unit)
            = mImageDataSource.addRunsheet(requestBody, res)

    override suspend fun uploadCollected(requestBody: RequestBody, res: (SimpleResponse?) -> Unit)
            = mImageDataSource.uploadCollected(requestBody, res)

    override suspend fun uploadDeposited(requestBody: RequestBody, res: (SimpleResponse?) -> Unit)
            = mImageDataSource.uploadDeposited(requestBody, res)


    override suspend fun uploadProfilePic(requestBody: RequestBody, res: (ProfilePicResponse?) -> Unit)
            = mImageDataSource.uploadProfilePic(requestBody, res)

}