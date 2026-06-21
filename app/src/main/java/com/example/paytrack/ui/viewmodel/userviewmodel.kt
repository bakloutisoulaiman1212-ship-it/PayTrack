package com.example.paytrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paytrack.data.localuser.User
import com.example.paytrack.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    fun register(username: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {

            if (repository.userExists(username)) {
                onResult(false)
            } else {
                repository.register(User(username = username, password = password))
                onResult(true)
            }
        }
    }

    fun login(username: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {

            val user = repository.login(username, password)
            onResult(user != null)
        }
    }
    fun changePassword(
        username: String,
        old: String,
        new: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val success = repository.changePassword(username, old, new)
            onResult(success)
        }
    }
}
