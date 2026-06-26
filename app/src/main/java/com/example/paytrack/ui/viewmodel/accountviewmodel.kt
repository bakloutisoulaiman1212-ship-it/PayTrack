package com.example.paytrack.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paytrack.data.localaccount.Account
import com.example.paytrack.data.localnotifications.Notification
import com.example.paytrack.data.localtransaction.Transaction
import com.example.paytrack.data.repository.AccountRepository
import com.example.paytrack.data.repository.NotificationRepository
import com.example.paytrack.data.repository.TransactionRepository
import com.example.paytrack.utils.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class AccountViewModel(
    private val repository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    private val context: Context ,
    private val notificationRepository: NotificationRepository,
    private val budgetViewModel : BudgetViewModel
) : ViewModel() {

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts: StateFlow<List<Account>> = _accounts

    fun loadAccounts(username: String) {
        viewModelScope.launch {
            repository.getAccountsByUser(username).collect {
                _accounts.value = it
            }
        }
    }

    fun addAccount(name: String, balance: Double, username: String) {
        viewModelScope.launch {
            repository.addAccount(
                Account(
                    name = name,
                    balance = balance,
                    username = username
                )
            )
            NotificationHelper.send(context, "Account Added ✅", "$name has been created")

            val notification = Notification(
                title = "Account Created 🆕",
                message = "Account added",
                timestamp = System.currentTimeMillis()
            )
            notificationRepository.insert(notification)
        }
    }

    fun deleteAccount(account: Account) {
        viewModelScope.launch {
            repository.deleteAccount(account)
            NotificationHelper.send(context, "Account Deleted 🗑️", "${account.name} has been deleted")

            val notification = Notification(
                title = "Account Deleted 🗑️",
                message = "Account deleted",
                timestamp = System.currentTimeMillis()
            )

            notificationRepository.insert(notification)

        }
    }

    fun updateAccount(account: Account) {
        viewModelScope.launch {
            repository.updateAccount(account)
            NotificationHelper.send(context, "Account Updated ✏️", "${account.name} has been updated")

            val notification = Notification(
                title = "Account Updated ✏️",
                message = "Account info updated",
                timestamp = System.currentTimeMillis()
            )

            notificationRepository.insert(notification)

        }
    }

    fun getAccountById(id: Long): Account? {
        return accounts.value.find { it.id == id }
    }

    // ✅ Transfer FIXED
    fun transfer(fromId: Long, toId: Long, amount: Double) {

        if (fromId == toId) return
        if (amount <= 0) return

        val from = getAccountById(fromId)
        val to = getAccountById(toId)

        if (from == null || to == null) return

        if (from.balance >= amount) {

            viewModelScope.launch {

                val updatedFrom = from.copy(balance = from.balance - amount)
                val updatedTo = to.copy(balance = to.balance + amount)

                repository.updateAccount(updatedFrom)
                repository.updateAccount(updatedTo)

                transactionRepository.insert(
                    Transaction(
                        accountId = fromId,
                        amount = amount,
                        type = "TRANSFER_OUT",
                        toAccountName = to.name,
                        fromAccountName = null,
                        date = System.currentTimeMillis()
                    )
                )

                transactionRepository.insert(
                    Transaction(
                        accountId = toId,
                        amount = amount,
                        type = "TRANSFER_IN",
                        fromAccountName = from.name,
                        toAccountName = null,
                        date = System.currentTimeMillis()
                    )
                )
                NotificationHelper.send(context, "Transfer Done 💸", "$amount DT transferred successfully")

                val notification = Notification(
                    title = "Transfer Done 🔄",
                    message = "$amount DT transferred",
                    timestamp = System.currentTimeMillis()
                )

                notificationRepository.insert(notification)}
        }
    }
    fun pay(accountId: Long, amount: Double) {
        if (amount <= 0) return

        val account = getAccountById(accountId)
        if (account == null) return

        if (account.balance >= amount) {
            viewModelScope.launch {
                val updated = account.copy(balance = account.balance - amount)
                repository.updateAccount(updated)

                transactionRepository.insert(
                    Transaction(
                        accountId = accountId,
                        amount = amount,
                        type = "PAYMENT",
                        fromAccountName = null,
                        toAccountName = null,
                        date = System.currentTimeMillis()
                    )
                )
                NotificationHelper.send(context, "Payment Done 💳", "$amount DT payment completed")


                val notification = Notification(
                    title = "Payment Done 💳",
                    message = "$amount DT payment completed",
                    timestamp = System.currentTimeMillis()
                )

                notificationRepository.insert(notification)
                budgetViewModel.loadData()
                delay(100)
                budgetViewModel.checkBudgetAlerts(context)

            }
        }
        }
    }