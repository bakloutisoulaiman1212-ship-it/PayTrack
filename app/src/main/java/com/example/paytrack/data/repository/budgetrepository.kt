package com.example.paytrack.data.repository

import com.example.paytrack.data.localbudget.Budget
import com.example.paytrack.data.localbudget.BudgetDao
import com.example.paytrack.data.localtransaction.TransactionDao
import java.util.Calendar

class BudgetRepository(
    private val budgetDao: BudgetDao,
    private val transactionDao: TransactionDao,
) {
    suspend fun setBudget(limit: Double) {
        budgetDao.insertBudget(Budget(monthlyLimit = limit))
    }
    suspend fun getBudget(): Budget? {
        return budgetDao.getBudget()
    }
    suspend fun getMonthlySpent(): Double {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfMonth = calendar.timeInMillis
        return transactionDao.getMonthlyPayments(calendar.timeInMillis)
    }
}
