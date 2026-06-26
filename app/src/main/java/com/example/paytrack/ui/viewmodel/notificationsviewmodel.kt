package com.example.paytrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paytrack.data.localnotifications.Notification
import com.example.paytrack.data.repository.NotificationRepository
import com.example.paytrack.data.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

// NotificationViewModel.kt
class NotificationViewModel(

    private val transactionRepository: TransactionRepository,
    private val accountViewModel: AccountViewModel,

    private val repository: NotificationRepository
) : ViewModel() {

    val notifications: Flow<List<Notification>> = repository.allNotifications
    val unreadCount: Flow<Int> = repository.unreadCount

    fun insert(title: String, message: String) {
        viewModelScope.launch {
            repository.insert(Notification(title = title, message = message))
        }
    }

    fun delete(notification: Notification) {
        viewModelScope.launch {
            repository.delete(notification)
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }
}