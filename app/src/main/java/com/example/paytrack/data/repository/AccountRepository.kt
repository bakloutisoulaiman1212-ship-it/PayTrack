package com.example.paytrack.data.repository

import com.example.paytrack.data.local.Account
import com.example.paytrack.data.local.AccountDao

class AccountRepository(private val dao: AccountDao) {

    val accounts = dao.getAllAccounts()

    suspend fun addAccount(account: Account) {
        dao.insert(account)
    }

    suspend fun updateAccount(account: Account) {
        dao.update(account)
    }

    suspend fun deleteAccount(account: Account) {
        dao.delete(account)
    }
}