package com.example.paytrack.data.localnotifications

import androidx.room.Entity
import androidx.room.PrimaryKey

// AppNotification.kt — في localnotification/
@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)