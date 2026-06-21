package com.example.paytrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.paytrack.data.localaccount.AccountDatabase
import com.example.paytrack.data.localuser.SessionManager
import com.example.paytrack.data.localuser.UserDatabase
import com.example.paytrack.data.repository.AccountRepository
import com.example.paytrack.data.repository.UserRepository
import com.example.paytrack.ui.navigation.AppNavGraph
import com.example.paytrack.ui.theme.PayTrackTheme
import com.example.paytrack.ui.viewmodel.AccountViewModel
import com.example.paytrack.ui.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ✅ account_database
        val accountdb = AccountDatabase.getDatabase(this)
        val dao = accountdb.accountDao()
        // ✅ repository
        val accrepository = AccountRepository(dao)
        // ✅ viewmodel
        val accountViewModel = AccountViewModel(accrepository)


        //user db
        val userdb = UserDatabase.getDatabase(this)

        val userrepository = UserRepository(userdb.userDao())
        val userViewModel = UserViewModel(userrepository)

        setContent {
            val context = LocalContext.current
            val session = SessionManager(context)

            val isDarkMode = remember { mutableStateOf(session.getDarkMode()) }

            PayTrackTheme(darkTheme = isDarkMode.value) {
                AppNavGraph(
                    accountViewModel = accountViewModel,
                    userViewModel = userViewModel,
                    isDarkMode = isDarkMode.value,
                    onToggleTheme = { isDark ->
                        isDarkMode.value = isDark
                        session.saveDarkMode(isDark)
                    }
                )
            }
        }

    }
}
