package com.example.planter_app.push_notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i("FCMToken", "Refreshed FCM token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("FCMMessage", "Message data payload: ${remoteMessage.data}")

            // Check if message contains a notification payload.
            remoteMessage.notification?.let {
                Log.d("FCMMessage", "Message Notification Body: ${it.body}")

                handleNotification(it.title ?: "", it.body ?: "")

            }
        }
    }

    private fun handleNotification(title: String, body: String) {
        val notificationUtils = NotificationUtils()
        if (!notificationUtils.areNotificationsEnabled()) {
            // Prompt the user to enable notifications
            notificationUtils.promptForNotificationPermission()
        } else {
            // Show the notification
            val notification = MyNotification(applicationContext, title, body)
            notification.fireNotification()
        }
    }
}