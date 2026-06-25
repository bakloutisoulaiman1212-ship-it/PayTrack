package com.example.paytrack.data.repository

import com.example.paytrack.data.localtransaction.TransactionDao
import com.example.paytrack.data.localtransaction.Transaction
import java.util.Calendar

class TransactionRepository(
    private val transactionDao: TransactionDao
) {
    // ✅ Insert transaction
    suspend fun insert(transaction: Transaction) {
        transactionDao.insert(transaction)
    }

    // ✅ Get all transactions
    fun getAllTransactions() = transactionDao.getAllTransactions()

    // ✅ Get transactions
    fun getTransactionsByAccount(accountId: Long) =
        transactionDao.getTransactionsByAccount(accountId)

    // ✅ Delete (optional)
    suspend fun delete(transaction: Transaction) {
        transactionDao.delete(transaction)
    }

    // ✅ Delete all (optional)
    suspend fun deleteAll() {
        transactionDao.deleteAll()
    }

    suspend fun getMonthlyPayments(): Double {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfMonth = calendar.timeInMillis
        return transactionDao.getMonthlyPayments(startOfMonth)
    }
}