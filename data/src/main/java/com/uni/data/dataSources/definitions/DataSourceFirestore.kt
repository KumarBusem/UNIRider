package com.uni.data.dataSources.definitions

import com.uni.data.models.Settings

abstract class DataSourceFirestore : BaseDataSource() {

    abstract fun getAppSettings(res: (Settings?) -> Unit)

}