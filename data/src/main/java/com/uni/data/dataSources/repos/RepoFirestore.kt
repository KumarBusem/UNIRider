package com.uni.data.dataSources.repos

import com.uni.data.dataSources.definitions.DataSourceFirestore
import com.uni.data.internal.dataSourceImpls.DataSourceImplFirestore
import com.uni.data.models.Settings

class RepoFirestore : DataSourceFirestore() {

    private val mUserDataSource: DataSourceFirestore by lazy { DataSourceImplFirestore() }

    override fun getAppSettings(res: (Settings?) -> Unit) = mUserDataSource.getAppSettings(res)

}