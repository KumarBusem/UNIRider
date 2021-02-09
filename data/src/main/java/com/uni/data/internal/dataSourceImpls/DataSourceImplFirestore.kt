package com.uni.data.internal.dataSourceImpls

import android.util.Log
import com.uni.data.dataSources.definitions.DataSourceFirestore
import com.uni.data.models.Settings

internal class DataSourceImplFirestore : DataSourceFirestore() {



    override fun getAppSettings(res: (Settings?) -> Unit) {

        SETTINGS.addSnapshotListener { snapshot, error ->

            Log.e("DATA SETTINGS:::", "Snapshot::$snapshot,\nError::$error")

            if (error != null) res(null)

            if (snapshot != null && snapshot.exists()) res(snapshot.toObject(Settings::class.java))

            else res(null)
        }
    }


}