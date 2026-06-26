package com.example.paytrack.data.repository

import com.example.paytrack.data.localnotifications.Notification
import com.example.paytrack.data.localnotifications.NotificationDao
import kotlinx.coroutines.flow.Flow

// NotificationRepository.kt
class NotificationRepository(private val dao: NotificationDao) {

    val allNotifications: Flow<List<Notification>> = dao.getAllNotifications()
    val unreadCount: Flow<Int> = dao.getUnreadCount()

    suspend fun insert(notification: Notification) = dao.insert(notification)
    suspend fun delete(notification: Notification) = dao.delete(notification)
    suspend fun clearAll() = dao.clearAll()
}