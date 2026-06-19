package com.example.paytrack.ui.screens.dashboard


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.paytrack.ui.viewmodel.AccountViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp


@Composable
fun DashboardScreen(
    viewModel: AccountViewModel,
    navController: NavController
) {

    val accounts by viewModel.accounts.collectAsState()

    val totalBalance = accounts.sumOf { it.balance }

    val maxAccount = accounts.maxByOrNull { it.balance }
    val minAccount = accounts.minByOrNull { it.balance }

    val topAccounts = accounts
        .sortedByDescending { it.balance }
        .take(3)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                "Dashboard",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Total Balance
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Total Balance")
                Text(
                    "$totalBalance DT",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ✅ Number of accounts
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Accounts")
                Text("${accounts.size}", style = MaterialTheme.typography.titleLarge)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ✅ Top 3 accounts
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text("Top 3 Accounts")

                topAccounts.forEach {
                    Text("${it.name} - ${it.balance} DT")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ✅ Biggest account
        maxAccount?.let {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Top Account")
                    Text("${it.name} - ${it.balance} DT")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ✅ Lowest account
        minAccount?.let {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Lowest Account")
                    Text("${it.name} - ${it.balance} DT")
                }
            }
        }
    }
}