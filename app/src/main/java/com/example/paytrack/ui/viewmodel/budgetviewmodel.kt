package com.example.paytrack.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.paytrack.data.localbudget.Budget
import com.example.paytrack.data.localnotifications.Notification
import com.example.paytrack.data.repository.BudgetRepository
import com.example.paytrack.data.repository.NotificationRepository
import com.example.paytrack.data.repository.TransactionRepository
import com.example.paytrack.utils.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// BudgetViewModel.kt
class BudgetViewModel(
    private val context: Context,
    private val budgetRepository: BudgetRepository,
    private val repository: NotificationRepository
) : ViewModel() {

    private val _budget = MutableStateFlow<Budget?>(null)
    private var warned80 = false
    private var warned90 = false

    val budget: StateFlow<Budget?> = _budget

    private val _spent = MutableStateFlow(0.0)
    val spent: StateFlow<Double> = _spent

    init { loadData() }

    fun loadData() {
        viewModelScope.launch {
            _budget.value = budgetRepository.getBudget()
            _spent.value = budgetRepository.getMonthlySpent()
            val status = getStatus()
            when (status) {
                BudgetStatus.WARNING -> NotificationHelper.send(
                    context, "⚡ Budget Warning", "You've used 80% of your monthly budget!"
                )
                BudgetStatus.DANGER -> NotificationHelper.send(
                    context, "🚨 Budget Danger", "You've used 90% of your monthly budget!"
                )
                BudgetStatus.EXCEEDED -> NotificationHelper.send(
                    context, "⚠️ Budget Exceeded", "You've exceeded your monthly budget!"
                )
                else -> {}
            }
        }
    }

    fun setBudget(limit: Double) {
        viewModelScope.launch {
            budgetRepository.setBudget(limit)
            loadData()
        }
    }

    fun getRemaining(): Double {
        val limit = _budget.value?.monthlyLimit ?: return 0.0
        return (limit - _spent.value).coerceAtLeast(0.0)
    }

    fun getRatio(): Float {
        val limit = _budget.value?.monthlyLimit ?: return 0f
        if (limit == 0.0) return 0f
        return (_spent.value / limit).toFloat().coerceAtMost(1f)
    }

    fun getStatus(): BudgetStatus {
        return when {
            getRatio() >= 1.0f -> BudgetStatus.EXCEEDED
            getRatio() >= 0.9f -> BudgetStatus.DANGER
            getRatio() >= 0.8f -> BudgetStatus.WARNING
            else              -> BudgetStatus.SAFE
        }
    }
    fun checkBudgetAlerts(context: Context) {

        val ratio = getRatio()

        viewModelScope.launch {

            // ✅ 80%
            if (ratio >= 0.8 && !warned80) {

                warned80 = true

                NotificationHelper.send(
                    context,
                    "Budget Warning ⚠️",
                    "You used 80% of your budget"
                )

                val notification = Notification(
                    title = "Budget Warning ⚠️",
                    message = "You used 80% of your budget",
                    timestamp = System.currentTimeMillis()
                )

                repository.insert(notification)
            }

            // ✅ 90%
            if (ratio >= 0.9 && !warned90) {

                warned90 = true

                NotificationHelper.send(
                    context,
                    "Budget Danger 🚨",
                    "Almost exceeded your budget"
                )

                val notification = Notification(
                    title = "Budget Danger 🚨",
                    message = "Almost exceeded your budget",
                    timestamp = System.currentTimeMillis()
                )

                repository.insert(notification)
            }
            if (ratio >= 1.0) {
                NotificationHelper.send(
                    context,
                    "Budget Exceeded ❌",
                    "You exceeded your budget!"
                )
                val notification = Notification(
                    title ="Budget Exceeded ❌",
                    message = "You exceeded your budget!",
                    timestamp = System.currentTimeMillis()
                )

                repository.insert(notification)
            }

        }
    }

}

enum class BudgetStatus(val label: String, val emoji: String) {
    SAFE("Safe", "✅"),
    WARNING("Warning", "⚡"),
    DANGER("Danger", "🚨"),
    EXCEEDED("Exceeded", "⚠️")
}
