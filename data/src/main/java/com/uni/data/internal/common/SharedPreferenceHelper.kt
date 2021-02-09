package com.uni.data.internal.common

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

internal class SharedPreferenceHelper private constructor(context: Context) {

    val mGson: Gson by lazy { Gson() }

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    fun putInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, default: Int = -1): Int {
        return sharedPreferences.getInt(key, default)
    }

    fun putLong(key: String, value: Long) {
        sharedPreferences.edit().putLong(key, value).apply()
    }

    fun getLong(key: String, defValue: Long = -1): Long? {
        val value = sharedPreferences.getLong(key, -1)
        return if (value == defValue) null else value
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun putStringSet(key: String, value: Set<String>) {
        sharedPreferences.edit().putStringSet(key, value).apply()
    }

    fun getStringSet(key: String): Set<String>? {
        return sharedPreferences.getStringSet(key, setOf())
    }

    fun putObject(key: String, value: Any) {
        putString(key, mGson.toJson(value))
    }

    inline fun <reified T> getObject(key: String): T? {
        val data = getString(key)
        if (data.isNullOrEmpty()) return null
        return mGson.fromJson<T>(data, object : TypeToken<T>() {}.type)
    }

    fun remove(vararg keys: String) {
        keys.forEach {
            sharedPreferences.edit().remove(it).apply()
        }
    }

    companion object {

        private var instance: SharedPreferenceHelper? = null

        fun initialize(context: Context) {
            if (instance == null) {
                instance = SharedPreferenceHelper(context)
            }
        }

        fun getInstance(): SharedPreferenceHelper {
            checkNotNull(instance) { "SharedPreferences not initialized" }
            return instance!!
        }
    }
}