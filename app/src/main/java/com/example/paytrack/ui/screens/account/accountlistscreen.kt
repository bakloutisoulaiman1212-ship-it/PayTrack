package com.example.paytrack.ui.screens.account


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.paytrack.data.localaccount.Account
import com.example.paytrack.ui.viewmodel.AccountViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.paytrack.data.localuser.SessionManager

@Composable
fun AccountListScreen(
    viewModel: AccountViewModel,
    onAddClick: () -> Unit,
    onAccountClick: (Account) -> Unit,
    onBackClick: () -> Unit
) {

    val accounts by viewModel.accounts.collectAsState()

    val primaryBlue = Color(0xFF3B82F6)
    val background = Color(0xFFF8FAFC)
    val context = LocalContext.current
    val session = SessionManager(context)

    LaunchedEffect(Unit) {
        val username = session.getUsername() ?: ""
        viewModel.loadAccounts(username)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            // ✅ TOP BAR
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = { onBackClick() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    "Accounts",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ LIST
            LazyColumn {
                items(accounts) { account ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable { onAccountClick(account) },

                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface                        ),

                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            // ✅ Name
                            Text(
                                text = account.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground                            )

                            // ✅ Balance
                            Text(
                                text = "${account.balance} DT",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }

        // ✅ FAB
        FloatingActionButton(
            onClick = onAddClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),

            containerColor = MaterialTheme.colorScheme.primary ,
            contentColor = Color.White
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}