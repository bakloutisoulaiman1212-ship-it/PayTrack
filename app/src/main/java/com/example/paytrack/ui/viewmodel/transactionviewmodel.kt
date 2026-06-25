package com.example.paytrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paytrack.data.localtransaction.Transaction
import com.example.paytrack.data.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val repository: TransactionRepository,
    private val accountViewModel: AccountViewModel
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

    fun voidTransaction(txn: Transaction) {
        viewModelScope.launch {

            if (txn.type == "PAYMENT") {
                val account = accountViewModel.getAccountById(txn.accountId)

                if (account != null) {
                    val updatedAccount = account.copy(
                        balance = account.balance + txn.amount
                    )
                    accountViewModel.updateAccount(updatedAccount)
                }

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
            }

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
            }
        }
    }
}