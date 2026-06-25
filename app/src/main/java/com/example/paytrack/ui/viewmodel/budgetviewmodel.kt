package com.example.paytrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.paytrack.data.localbudget.Budget
import com.example.paytrack.data.repository.BudgetRepository
import com.example.paytrack.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// BudgetViewModel.kt
class BudgetViewModel(
    private val budgetRepository: BudgetRepository
) : ViewModel() {

    private val _budget = MutableStateFlow<Budget?>(null)
    val budget: StateFlow<Budget?> = _budget

    private val _spent = MutableStateFlow(0.0)
    val spent: StateFlow<Double> = _spent

    init { loadData() }

    fun loadData() {
        viewModelScope.launch {
            _budget.value = budgetRepository.getBudget()
            _spent.value = budgetRepository.getMonthlySpent()
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
}

enum class BudgetStatus(val label: String, val emoji: String) {
    SAFE("Safe", "✅"),
    WARNING("Warning", "⚡"),
    DANGER("Danger", "🚨"),
    EXCEEDED("Exceeded", "⚠️")
}