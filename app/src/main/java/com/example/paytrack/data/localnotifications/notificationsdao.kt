package com.example.paytrack.data.localnotifications

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// NotificationDao.kt
@Dao
interface NotificationDao {

    @Insert
    suspend fun insert(notification: Notification)

    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<Notification>>

    @Delete
    suspend fun delete(notification: Notification)

    @Query("DELETE FROM notifications")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM notifications")
    fun getUnreadCount(): Flow<Int>
}