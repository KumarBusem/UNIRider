package com.dcartlogistics.data.dataSources.definitions

import com.dcartlogistics.data.dataSources.repos.RepoSharedPreferences
import com.dcartlogistics.data.internal.common.MyApi
import com.dcartlogistics.data.BuildConfig
import com.dcartlogistics.data.internal.common.SafeApiRequest
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

abstract class BaseDataSource: SafeApiRequest() {

    public val repoPrefs: DataSourceSharedPreferences by lazy { RepoSharedPreferences() }

    protected val TAG: String = javaClass.simpleName

    protected val mFirestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    protected val mFirebaseStorageReference: StorageReference by lazy { FirebaseStorage.getInstance().reference }
    protected val SETTINGS: DocumentReference = mFirestoreInstance.collection(COLLECTION_APP_SETTINGS).document("settings")

    protected val API: MyApi by lazy { MyApi.invoke()}

    companion object {

        private val COLLECTION_APP_SETTINGS: String = "${BuildConfig.bucket}_app_settings"
    }

}