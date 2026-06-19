package com.example.paytrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.paytrack.data.local.AccountDatabase
import com.example.paytrack.data.repository.AccountRepository
import com.example.paytrack.ui.navigation.AppNavGraph
import com.example.paytrack.ui.viewmodel.AccountViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ✅ database
        val database = AccountDatabase.getDatabase(this)
        val dao = database.accountDao()

        // ✅ repository
        val repository = AccountRepository(dao)

        // ✅ viewmodel
        val viewModel = AccountViewModel(repository)

        setContent {
            AppNavGraph(viewModel)
        }
    }
}
