package com.example.paytrack.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paytrack.data.localnotifications.Notification
import com.example.paytrack.data.localtransaction.Transaction
import com.example.paytrack.data.repository.NotificationRepository
import com.example.paytrack.data.repository.TransactionRepository
import com.example.paytrack.utils.NotificationHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val repository: TransactionRepository,
    private val accountViewModel: AccountViewModel,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    val allTransactions: Flow<List<Transaction>> =
        repository.getAllTransactions()

    fun getTransactionsByAccount(accountId: Long): Flow<List<Transaction>> {
        return repository.getTransactionsByAccount(accountId)
    }

    fun insertTransaction(accountId: Long, amount: Double, type: String) {
        val transaction = Transaction(
            accountId = accountId,
            amount = amount,
            type = type,
            fromAccountName = null,
            toAccountName = null,
            date = System.currentTimeMillis()
        )
        viewModelScope.launch {
            repository.insert(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.delete(transaction)
        }
    }

    fun deleteAllTransactions() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    fun voidTransaction(txn: Transaction, context: Context) {

        viewModelScope.launch {

            // ✅ 1. reverse payment
            if (txn.type == "PAYMENT") {
                val account = accountViewModel.getAccountById(txn.accountId)
                if (account != null) {
                    accountViewModel.updateAccount(
                        account.copy(balance = account.balance + txn.amount)
                    )
                }
            }

            // ✅ 2. reverse transfer
            if (txn.type == "TRANSFER_OUT") {
                val toAccount = accountViewModel.accounts.value
                    .find { it.name == txn.toAccountName }

                if (toAccount != null) {
                    accountViewModel.transfer(
                        fromId = toAccount.id,
                        toId = txn.accountId,
                        amount = txn.amount
                    )
                }
            }

            // ✅ 3. delete original
            repository.delete(txn)

            // ✅ 4. insert VOID transaction
            repository.insert(
                Transaction(
                    accountId = txn.accountId,
                    amount = txn.amount,
                    type = "VOID",
                    fromAccountName = null,
                    toAccountName = null,
                    date = System.currentTimeMillis()
                )
            )

            // ✅ 5. show system notification 🔔
            NotificationHelper.send(
                context,
                "Transaction Voided ❌",
                "${txn.amount} DT operation cancelled"
            )

            // ✅ 6. save in DB ✅🔥
            val notification = Notification(
                title = "Transaction Voided ❌",
                message = "${txn.amount} DT operation cancelled",
                timestamp = System.currentTimeMillis()
            )

            notificationRepository.insert(notification)
        }
    }

}