package com.example.planter_app.push_notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import android.provider.Settings
import com.example.planter_app.MainActivity
import com.example.planter_app.MyApplication
import com.example.planter_app.R
import com.example.planter_app.ui.screens.settings.SettingsViewModel
import com.example.planter_app.utilities.showToast

class MyNotification(private val context: Context, private val title: String, private val msg: String) {

    private val channelID = "FCM100"
    private val channelName = "FCMMessage"

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun fireNotification() {
        createNotificationChannelIfNeeded()

        // upon clicking the notification, open the app (home screen)
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)


        val notificationBuilder = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(msg)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        try {
            notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
            Log.d("MyNotification", "Notification fired successfully.")
        } catch (e: Exception) {
            Log.e("MyNotification", "Failed to fire notification", e)
        }
    }

    private fun createNotificationChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val existingChannel = notificationManager.getNotificationChannel(channelID)
            if (existingChannel == null) {
                val notificationChannel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(notificationChannel)
                Log.d("MyNotification", "Notification channel created.")
            } else {
                Log.d("MyNotification", "Notification channel already exists.")
            }
        }
    }
}


class NotificationUtils() {
    val context: Context = MyApplication.instance!!.applicationContext
    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun areNotificationsEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.areNotificationsEnabled()
        } else {
            // For SDK versions lower than Oreo, assume notifications are always enabled
            true
        }
    }

    fun promptForNotificationPermission() {
            showToast(context, "Please enable/disable notifications for this app in settings")
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
    }
}