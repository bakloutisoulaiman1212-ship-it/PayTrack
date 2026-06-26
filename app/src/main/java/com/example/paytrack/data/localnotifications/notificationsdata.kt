package com.example.paytrack.data.localnotifications

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// NotificationDatabase.kt
@Database(entities = [Notification::class], version = 1)
abstract class NotificationDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile private var INSTANCE: NotificationDatabase? = null

        fun getDatabase(context: Context): NotificationDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    NotificationDatabase::class.java,
                    "notification_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}