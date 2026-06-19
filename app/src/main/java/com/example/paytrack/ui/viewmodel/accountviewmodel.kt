package com.example.paytrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paytrack.data.local.Account
import com.example.paytrack.data.repository.AccountRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AccountViewModel(
    private val repository: AccountRepository
) : ViewModel() {

    // ✅ list
    val accounts = repository.accounts
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // ✅ add
    fun addAccount(name: String, balance: Double) {
        viewModelScope.launch {
            repository.addAccount(Account(name = name, balance = balance))
        }
    }

    // ✅ delete
    fun deleteAccount(account: Account) {
        viewModelScope.launch {
            repository.deleteAccount(account)
        }
    }

    // ✅ update
    fun updateAccount(account: Account) {
        viewModelScope.launch {
            repository.updateAccount(account)
        }
    }

    // ✅ get by id
    fun getAccountById(id: Long): Account? {
        return accounts.value.find { it.id == id }
    }
}
