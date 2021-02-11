package com.uni.rider.common

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.uni.rider.MainActivity
import com.uni.rider.R

class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d(TAG, "${remoteMessage.notification}")
        showNotification(remoteMessage.notification)
    }

    private fun showNotification(remoteMessage: RemoteMessage.Notification?) {

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "NotificationsData", NotificationManager.IMPORTANCE_MAX)
            notificationChannel.enableLights(true)
            notificationChannel.vibrationPattern = longArrayOf(400, 100, 400, 100, 400)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.uni_trans)
                .setContentTitle(remoteMessage?.title)
                .setAutoCancel(true)
                .setOngoing(true)
                .setContentText(remoteMessage?.body)
                .setSubText("Click here to open")
                .setContentIntent(getPendingIntent(remoteMessage?.title))
                .setStyle(NotificationCompat.BigTextStyle())
                .setPriority(Notification.PRIORITY_MAX)

        notificationManager.notify(1, notificationBuilder.build())
    }

    private fun getPendingIntent(title: String?): PendingIntent {

        return NavDeepLinkBuilder(applicationContext)
                .setComponentName(MainActivity::class.java)
                .setGraph(R.navigation.navigation_graph)
                .setDestination(if(title?.contains("Upload Runsheets") == true) R.id.addRunsheetFragment else R.id.homeFragment)
                .createPendingIntent()
    }


    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
        private const val NOTIFICATION_CHANNEL_ID = "101"
    }
}