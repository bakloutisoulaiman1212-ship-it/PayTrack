package com.example.paytrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paytrack.data.localaccount.Account
import com.example.paytrack.data.repository.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AccountViewModel(
    private val repository: AccountRepository
) : ViewModel() {

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts: StateFlow<List<Account>> = _accounts

    // ✅ load accounts حسب user
    fun loadAccounts(username: String) {
        viewModelScope.launch {
            repository.getAccountsByUser(username).collect {
                _accounts.value = it
            }
        }
    }

    // ✅ add
    fun addAccount(name: String, balance: Double, username: String) {
        viewModelScope.launch {
            repository.addAccount(
                Account(
                    name = name,
                    balance = balance,
                    username = username
                )
            )
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