package com.example.paytrack.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.paytrack.data.localaccount.Account
import com.example.paytrack.data.localuser.SessionManager
import com.example.paytrack.ui.viewmodel.AccountViewModel

@Composable
fun DashboardScreen(
    viewModel: AccountViewModel,
    navController: NavController
) {

    val accounts by viewModel.accounts.collectAsState()
    val context = LocalContext.current
    val session = SessionManager(context)

    LaunchedEffect(Unit) {
        val username = session.getUsername() ?: ""
        viewModel.loadAccounts(username)
    }

    val totalBalance = accounts.sumOf { it.balance }
    val maxAccount = accounts.maxByOrNull { it.balance }
    val minAccount = accounts.minByOrNull { it.balance }

    val topAccounts = accounts
        .sortedByDescending { it.balance }
        .take(3)

    val primaryBlue = Color(0xFF3B82F6)
    val background = Color(0xFFF8FAFC)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(16.dp)
    ) {

        // ✅ TOP BAR
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = primaryBlue
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                "Dashboard",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ TOTAL BALANCE
        DashboardCard("Total Balance", "$totalBalance DT", primaryBlue)

        Spacer(modifier = Modifier.height(12.dp))

        // ✅ ACCOUNTS COUNT
        DashboardCard("Accounts", "${accounts.size}", primaryBlue)

        Spacer(modifier = Modifier.height(12.dp))

        // ✅ MAX ACCOUNT
        maxAccount?.let {
            DashboardCard("Top Account", "${it.name} - ${it.balance} DT", primaryBlue)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ✅ MIN ACCOUNT
        minAccount?.let {
            DashboardCard("Lowest Account", "${it.name} - ${it.balance} DT", primaryBlue)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ CHART
        CardWhite {
            AccountsBarChart(accounts)
        }
    }
}

@Composable
fun DashboardCard(title: String, value: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = title,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                color = color,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun CardWhite(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
fun AccountsBarChart(accounts: List<Account>) {

    val maxBalance = accounts.maxOfOrNull { it.balance } ?: 1.0
    val primaryBlue = Color(0xFF3B82F6)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = "Accounts Overview",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {

            accounts.forEach { account ->

                val ratio = (account.balance / maxBalance).toFloat()

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    val maxHeight = 60
                    val minHeight = 10

                    val barHeight = (minHeight + (maxHeight * ratio)).coerceAtMost(maxHeight.toFloat())

                    Box(
                        modifier = Modifier
                            .height(barHeight.dp)
                            .width(30.dp)
                            .background(primaryBlue)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = account.name,
                        color = Color.Black,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )
                }
            }
        }
    }
}