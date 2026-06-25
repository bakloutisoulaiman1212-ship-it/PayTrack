package com.example.paytrack.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.paytrack.data.localaccount.Account
import com.example.paytrack.data.localbudget.Budget
import com.example.paytrack.data.localtransaction.Transaction
import com.example.paytrack.data.localuser.SessionManager
import com.example.paytrack.ui.viewmodel.AccountViewModel
import com.example.paytrack.ui.viewmodel.BudgetStatus
import com.example.paytrack.ui.viewmodel.BudgetViewModel
import com.example.paytrack.ui.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    accountViewModel: AccountViewModel,
    transactionViewModel: TransactionViewModel,
    budgetViewModel: BudgetViewModel,
    navController: NavController
) {

    val accounts by accountViewModel.accounts.collectAsState()
    val transactions by transactionViewModel
        .allTransactions
        .collectAsState(initial = emptyList())

    val context = LocalContext.current
    val session = SessionManager(context)

    LaunchedEffect(Unit) {
        val username = session.getUsername() ?: ""
        accountViewModel.loadAccounts(username)
    }

    val totalBalance = accounts.sumOf { it.balance }

    val spent = transactions
        .filter { it.type == "PAYMENT" }
        .sumOf { it.amount }

    val recentTransactions = transactions.take(3)
    val lastTransaction = transactions.firstOrNull()

    val maxAccount = accounts.maxByOrNull { it.balance }
    val minAccount = accounts.minByOrNull { it.balance }

    val payments = transactions.filter { it.type == "PAYMENT" }
    val daysOrder = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val grouped = payments
        .groupBy { formatDay(it.date) }
        .toList()
        .sortedBy { (day, _) -> daysOrder.indexOf(day) }

    val primaryBlue = Color(0xFF3B82F6)
    val background = Color(0xFFF8FAFC)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(16.dp)
    ) {

        item {
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
        }

        item {
            DashboardCard("Total Balance", "${String.format("%.2f", totalBalance)} DT", primaryBlue)
            Spacer(modifier = Modifier.height(12.dp))
        }
        item {
                BudgetSection(budgetViewModel)
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            DashboardCard("Accounts", "${accounts.size}", primaryBlue)
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            maxAccount?.let {
                DashboardCard("Top Account", "${it.name} - ${it.balance} DT", primaryBlue)
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            minAccount?.let {
                DashboardCard("Lowest Account", "${it.name} - ${it.balance} DT", primaryBlue)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            DashboardCard("Spent", "${String.format("%.2f", spent)} DT", Color.Red)
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            CardWhite {
                AccountsBarChart(accounts)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            CardWhite {
                TransactionsBarChart(transactions)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            CardWhite {

                Text("Recent Activity", style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(8.dp))

                recentTransactions.forEach { txn ->

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(txn.type)

                        Text(
                            "${txn.amount} DT",
                            color = when {
                                txn.type.contains("OUT") -> Color.Red
                                txn.type.contains("IN") -> primaryBlue
                                txn.type == "PAYMENT" -> Color(0xFF4CAF50)
                                else -> Color.Black
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

    @Composable
    fun DashboardCard(title: String, value: String, color: Color) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = title,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
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

                        val maxHeight = 80f
                        val minHeight = 10f

                        val barHeight =
                            (minHeight + (maxHeight * ratio)).coerceAtMost(maxHeight)

                        Box(
                            modifier = Modifier
                                .height(barHeight.dp)
                                .width(30.dp)
                                .background(primaryBlue)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

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

    @Composable
    fun CardWhite(content: @Composable ColumnScope.() -> Unit) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                content()
            }
        }
    }
@Composable
fun TransactionsBarChart(transactions: List<Transaction>) {

    val payments = transactions.filter { it.type == "PAYMENT" }

    val daysOrder = listOf(
        "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
    )

    val grouped = payments
        .groupBy { formatDay(it.date) }
        .toList()
        .sortedBy { (day, _) ->
            daysOrder.indexOf(day)
        }

    val maxAmount = grouped.maxOfOrNull { (_, txns) ->
        txns.sumOf { it.amount }
    } ?: 1.0

    val red = Color(0xFFF44336)

    Column {

        Text(
            text = "Spending Activity",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {

            grouped.forEach { (day, txns) ->

                val total = txns.sumOf { it.amount }

                val ratio = (total / maxAmount).toFloat()

                val height = (10 + (80 * ratio)).dp

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Box(
                        modifier = Modifier
                            .height(height)
                            .width(24.dp)
                            .background(red)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = day,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = "${total.toInt()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
fun formatDay(timestamp: Long): String {
    val sdf = SimpleDateFormat("EEE", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
@Composable
fun BudgetSection(viewModel: BudgetViewModel) {

    val budget by viewModel.budget.collectAsState()
    val spent  by viewModel.spent.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.loadData() }

    if (budget == null) {
        // ── لا يوجد بودجة بعد ──
        NoBudgetCard { showDialog = true }
    } else {
        // ── عرض البودجة ──
        BudgetCard(
            budget   = budget!!,
            spent    = spent,
            ratio    = viewModel.getRatio(),
            status   = viewModel.getStatus(),
            remaining = viewModel.getRemaining(),
            onEditClick = { showDialog = true }
        )
    }

    if (showDialog) {
        BudgetDialog(
            currentLimit = budget?.monthlyLimit,
            onConfirm = { limit ->
                viewModel.setBudget(limit)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

// ── Card: لا توجد بودجة ──
@Composable
fun NoBudgetCard(onSetClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("💰 No budget set", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onSetClick) {
                Text("Set Monthly Budget")
            }
        }
    }
}

// ── Card: البودجة ──
@Composable
fun BudgetCard(
    budget: Budget,
    spent: Double,
    ratio: Float,
    status: BudgetStatus,
    remaining: Double,
    onEditClick: () -> Unit
) {
    val statusColor = when (status) {
        BudgetStatus.SAFE     -> Color(0xFF4CAF50)
        BudgetStatus.WARNING  -> Color(0xFFFFC107)
        BudgetStatus.DANGER   -> Color(0xFFFF5722)
        BudgetStatus.EXCEEDED -> Color.Red
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Monthly Budget",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray
                )
                TextButton(onClick = onEditClick) {
                    Text("Edit", color = Color(0xFF3B82F6))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BudgetStat("Budget",    "${budget.monthlyLimit} DT", Color.Black)
                BudgetStat("Spent",     "${String.format("%.2f", spent)} DT", Color.Red)
                BudgetStat("Remaining", "${String.format("%.2f", remaining)} DT", statusColor)
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = ratio,
                color = statusColor,
                trackColor = Color(0xFFE0E0E0),
                modifier = Modifier.fillMaxWidth().height(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "${status.emoji} ${status.label}",
                    color = statusColor,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "${(ratio * 100).toInt()}% used",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun BudgetStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = color)
    }
}

// ── Dialog ──
@Composable
fun BudgetDialog(
    currentLimit: Double?,
    onConfirm: (Double) -> Unit,
    onDismiss: () -> Unit
) {
    var input by remember { mutableStateOf(currentLimit?.toString() ?: "") }
    val isEdit = currentLimit != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEdit) "Edit Budget" else "Set Monthly Budget") },
        text = {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Amount (DT)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = {
                val amount = input.toDoubleOrNull()
                if (amount != null && amount > 0) onConfirm(amount)
            }) {
                Text(if (isEdit) "Update" else "Set")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}