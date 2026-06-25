package com.example.paytrack.ui.screens.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.paytrack.data.localuser.SessionManager
import com.example.paytrack.ui.viewmodel.AccountViewModel
import com.example.paytrack.ui.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun HistoryScreen(
    transactionViewModel: TransactionViewModel,
    accountViewModel: AccountViewModel,
    navController: NavController
) {

    val transactions by transactionViewModel
        .allTransactions
        .collectAsState(initial = emptyList())

    val accounts by accountViewModel
        .accounts
        .collectAsState()
    val context = LocalContext.current
    val session = SessionManager(context)

    val username = session.getUsername() ?: ""
    LaunchedEffect(Unit) {
        accountViewModel.loadAccounts(username)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // ✅ HEADER
        Row(verticalAlignment = Alignment.CenterVertically) {

            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color(0xFF4A90E2)
                )
            }

            Text(
                text = "Transactions History",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ LIST (scrollable ✅)
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            accounts.forEach { account ->

                val accountTransactions = transactions.filter {
                    it.accountId == account.id                 }

                if (accountTransactions.isNotEmpty()) {

                    // ✅ عنوان الحساب
                    item {
                        Text(
                            text = "${account.name} History",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF4A90E2),
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }

                    // ✅ transactions متاع الحساب
                    items(accountTransactions) { txn ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                // ✅ TYPE + AMOUNT
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Text(
                                        text = txn.type,
                                        color = Color.Black
                                    )

                                    Text(
                                        text = "${txn.amount} DT",
                                        color = when {
                                            txn.type.contains("OUT") -> Color.Red
                                            txn.type.contains("IN") -> Color(0xFF4A90E2)
                                            txn.type == "PAYMENT" -> Color(0xFF4CAF50)
                                            txn.type == "VOID" -> Color.Gray
                                            else -> Color.Black
                                        }
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                // ✅ DETAILS
                                when (txn.type) {

                                    "TRANSFER_OUT" -> {
                                        Text("To: ${txn.toAccountName ?: "Unknown"}")
                                    }

                                    "TRANSFER_IN" -> {
                                        Text("From: ${txn.fromAccountName ?: "Unknown"}")
                                    }

                                    "PAYMENT" -> {
                                        Text("Payment")
                                    }

                                    "VOID" -> {
                                        Text(
                                            "Transaction canceled",
                                            color = Color.Gray
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))
// ✅ VOID BUTTON
                                if (txn.type == "PAYMENT" || txn.type == "TRANSFER_OUT" ) {

                                    Spacer(modifier = Modifier.height(8.dp))



                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End // ✅ على اليمين
                                    ) {

                                        Button(
                                            onClick = {
                                                transactionViewModel.voidTransaction(txn)
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color.Red // ✅ bouton rouge
                                            )
                                        ) {

                                            Text(
                                                text = "↩️",
                                                color = Color.White
                                            )

                                            Spacer(modifier = Modifier.width(4.dp))

                                            Text(
                                                text = "Void",
                                                color = Color.White // ✅ text blanc
                                            )
                                        }
                                    }
                                }


                                // ✅ DATE
                                Text(
                                    text = formatDate(txn.date),
                                    color = Color.Gray,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
