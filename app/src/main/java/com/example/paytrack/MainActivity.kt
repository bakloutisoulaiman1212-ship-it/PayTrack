package com.example.paytrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.paytrack.data.localaccount.AccountDatabase
import com.example.paytrack.data.localbudget.BudgetDatabase
import com.example.paytrack.data.localtransaction.TransactionDatabase
import com.example.paytrack.data.localuser.UserDatabase
import com.example.paytrack.data.repository.AccountRepository
import com.example.paytrack.data.repository.BudgetRepository
import com.example.paytrack.data.repository.TransactionRepository
import com.example.paytrack.data.repository.UserRepository
import com.example.paytrack.ui.navigation.AppNavGraph
import com.example.paytrack.ui.theme.PayTrackTheme
import com.example.paytrack.ui.viewmodel.AccountViewModel
import com.example.paytrack.ui.viewmodel.BudgetViewModel
import com.example.paytrack.ui.viewmodel.TransactionViewModel
import com.example.paytrack.ui.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ 1. User DB
        val userdb = UserDatabase.getDatabase(this)
        val userrepository = UserRepository(userdb.userDao())
        val userViewModel = UserViewModel(userrepository)

        // ✅ 2. Account DB
        val accountdb = AccountDatabase.getDatabase(this)
        val dao = accountdb.accountDao()
        val accountRepository = AccountRepository(dao)

        // ✅ 3. Transaction DB
        val transactionDb = TransactionDatabase.getDatabase(this)
        val transactionRepository = TransactionRepository(
            transactionDao = transactionDb.transactionDao()
        )

        // ✅ 4. Budget DB — زيد هذا
        val budgetDb = BudgetDatabase.getDatabase(this)
        val budgetRepository = BudgetRepository(
            budgetDao = budgetDb.budgetDao(),
            transactionDao = transactionDb.transactionDao()
        )
        val budgetViewModel = BudgetViewModel(budgetRepository)
        // ✅ 5. ViewModels
        val accountViewModel = AccountViewModel(accountRepository, transactionRepository)
        val transactionViewModel = TransactionViewModel(transactionRepository, accountViewModel)

        setContent {
            PayTrackTheme {
                AppNavGraph(
                    accountViewModel = accountViewModel,
                    userViewModel = userViewModel,
                    transactionViewModel = transactionViewModel,
                    budgetViewModel = budgetViewModel
                )
            }
        }
    }
}