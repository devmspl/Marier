package com.app.marier.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.app.marier.R
import com.app.marier.activities.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = MyFirebaseMessagingService::class.java.simpleName

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("heremytokrn", "Token: $token")
        Log.d("heremytokrn", "Tokeasasasasasas")

        // Upload the token to your server
        // You can store it in preferences
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "Notification: ${remoteMessage.from}")

        // Create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        // Check if push notification has notification payload or not
        remoteMessage.notification?.let { notification ->
            val title = notification.title
            val body = notification.body
            Log.d(TAG, "Notification Title: $title - Body: $body")
            showNotification(title, body)
        }

        // Check if push notification has data payload or not
        if (remoteMessage.data.isNotEmpty()) {
            for ((key, value) in remoteMessage.data) {
                Log.d(TAG, "Key: $key - Value: $value")
            }
            // Show notification if required
            // showNotification(title, body)
        }
    }

    private fun createNotificationChannel() {
        val channelId = "channel_id"
        val channelName = "Test Notification Channel"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, channelName, importance)
        channel.description = "My test notification channel"

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun showNotification(title: String?, body: String?) {
        val intent = Intent(this, SplashActivity::class.java)
        val channelId = "channel_id"
        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setAutoCancel(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder.build())
    }
}
