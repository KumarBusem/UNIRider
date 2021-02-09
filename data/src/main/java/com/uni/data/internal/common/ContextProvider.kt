package com.uni.data.internal.common

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.google.firebase.FirebaseApp


internal class ContextProvider : ContentProvider() {

    override fun onCreate(): Boolean {

//        val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(false).build()
//        FirebaseFirestore.getInstance().firestoreSettings = settings
//        Log.e("**IS PERSITENCE ENABLED", FirebaseFirestore.getInstance().firestoreSettings.isPersistenceEnabled.toString())

        context?.let { context ->
            FirebaseApp.initializeApp(context)
            SharedPreferenceHelper.initialize(context)
            return true
        }
        return false
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        return null
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}