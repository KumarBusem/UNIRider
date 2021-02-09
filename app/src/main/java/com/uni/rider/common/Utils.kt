package com.uni.rider.common

import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.view.Display
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible


fun getRandomUDID(): String = UUID.randomUUID().toString()

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}


fun isGpsAvailable(context: Context): Boolean {
    val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun String.isStatusSuccess(): Boolean {
    return this == "success"
}

fun getCurrentAppVersion(context: Context): String {
    val manager = context.packageManager
    val info = manager.getPackageInfo(context.packageName, 0)
    return info.versionName
}

fun getCurrentHalfScreen(activity: Activity, d: Double): Int {
    val display: Display = activity.windowManager.defaultDisplay
    val width = display.width
    return (width / d).toInt()
}


val KProperty0<*>.isLazyInitialized: Boolean
    get() {
        // Prevent IllegalAccessException from JVM access check
        isAccessible = true
        return (getDelegate() as Lazy<*>).isInitialized()
    }

fun getCurrentDateInServerFormat(): String = getTimeInServerFormat()

fun getTimeInServerFormat(): String {

    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    return simpleDateFormat.format(Date())
}