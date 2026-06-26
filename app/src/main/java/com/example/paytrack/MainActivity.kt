package com.example.paytrack

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.paytrack.data.localaccount.AccountDatabase
import com.example.paytrack.data.localbudget.BudgetDatabase
import com.example.paytrack.data.localnotifications.NotificationDatabase
import com.example.paytrack.data.localtransaction.TransactionDatabase
import com.example.paytrack.data.localuser.SessionManager
import com.example.paytrack.data.localuser.UserDatabase
import com.example.paytrack.data.repository.AccountRepository
import com.example.paytrack.data.repository.BudgetRepository
import com.example.paytrack.data.repository.NotificationRepository
import com.example.paytrack.data.repository.TransactionRepository
import com.example.paytrack.data.repository.UserRepository
import com.example.paytrack.ui.navigation.AppNavGraph
import com.example.paytrack.ui.theme.PayTrackTheme
import com.example.paytrack.ui.viewmodel.AccountViewModel
import com.example.paytrack.ui.viewmodel.BudgetViewModel
import com.example.paytrack.ui.viewmodel.NotificationViewModel
import com.example.paytrack.ui.viewmodel.TransactionViewModel
import com.example.paytrack.ui.viewmodel.UserViewModel
import com.example.paytrack.utils.NotificationHelper

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationHelper.init(this)
        val session = SessionManager(this)
        // ✅ Request permission (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
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
        val context: Context
        // ✅ 4. Budget DB — زيد هذا
        val budgetDb = BudgetDatabase.getDatabase(this)
        val budgetRepository = BudgetRepository(
            budgetDao = budgetDb.budgetDao(),
            transactionDao = transactionDb.transactionDao()
        )
        // ✅ 5. ViewModels
        val notificationDb = NotificationDatabase.getDatabase(this)
        val repository = NotificationRepository(notificationDb.notificationDao())
        val budgetViewModel = BudgetViewModel( this ,budgetRepository ,repository)

        val accountViewModel = AccountViewModel(accountRepository, transactionRepository, this , repository, budgetViewModel)

        val transactionViewModel = TransactionViewModel(transactionRepository, accountViewModel ,repository )
        val notificationViewModel = NotificationViewModel(transactionRepository ,accountViewModel ,repository)


        NotificationHelper.init(this)

        setContent {var darkMode by remember { mutableStateOf(session.getDarkMode()) }
            PayTrackTheme (darkTheme = darkMode) {
                AppNavGraph(
                    accountViewModel = accountViewModel,
                    userViewModel = userViewModel,
                    transactionViewModel = transactionViewModel,
                    budgetViewModel = budgetViewModel ,
                    notificationViewModel = notificationViewModel ,
                    darkMode = darkMode,
                    onToggleDarkMode = {
                        darkMode = it
                        session.saveDarkMode(it)}

                )
            }
        }
    }
}