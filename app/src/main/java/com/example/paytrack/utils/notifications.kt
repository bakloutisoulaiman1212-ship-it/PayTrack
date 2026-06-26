package com.example.paytrack.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.paytrack.R
import com.example.paytrack.ui.viewmodel.NotificationViewModel

object NotificationHelper {

    private const val CHANNEL_ID = "paytrack_channel"
    private const val CHANNEL_NAME = "PayTrack Alerts"
    private var notifId = 0
    private var notificationViewModel: NotificationViewModel? = null

    fun init(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "PayTrack notifications"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    fun send(context: Context, title: String, message: String) {
        notificationViewModel?.insert(title, message)

        // ✅ Push notification
        val manager = context.getSystemService(NotificationManager::class.java)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        manager.notify(notifId++, notification)
    }
}