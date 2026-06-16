package com.example.paytrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.paytrack.data.Account
import com.example.paytrack.data.AppDatabase
import com.example.paytrack.ui.theme.PayTrackTheme
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.padding
import kotlinx.coroutines.flow.first
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getDatabase(applicationContext)
        val dao = db.accountDao()

        lifecycleScope.launch {
            val accounts = dao.getAllAccounts().first()
            if (accounts.isEmpty()) {
                dao.insertAccount(Account(name = "Main Wallet", balance = 500.0))
                dao.insertAccount(Account(name = "Bank Card", balance = 1200.0))
            }
            dao.getAllAccounts().collect { accounts ->
                accounts.forEach {
                    android.util.Log.d("PayTrack", "Account: id=${it.id}, name=${it.name}, balance=${it.balance}")
                }
            }
        }

        setContent {
            PayTrackTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Text(
                        text = "PayTrack",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}