package com.example.paytrack.ui.screens.account



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.paytrack.ui.viewmodel.AccountViewModel


@Composable
fun EditAccountScreen(
    viewModel: AccountViewModel,
    accountId: Long,
    navController: NavController
) {

    val account = viewModel.getAccountById(accountId)

    if (account != null) {

        var name by remember { mutableStateOf(account.name) }
        var balance by remember { mutableStateOf(account.balance.toString()) }
        var showDialog by remember { mutableStateOf(false) }

        val primaryBlue = Color(0xFF3B82F6)
        val background = Color(0xFFF8FAFC)
        val dangerRed = Color(0xFFEF4444)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
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
                        tint = MaterialTheme.colorScheme.onBackground                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Edit Account",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ✅ CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {

                Column(modifier = Modifier.padding(16.dp)) {

                    // ✅ NAME
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Account Name") },
                        modifier = Modifier.fillMaxWidth() ,
                        colors = OutlinedTextFieldDefaults.colors(
                            // ✅ Text
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,

                            // ✅ Border
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),

                            // ✅ Label
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),

                            // ✅ Cursor
                            cursorColor = MaterialTheme.colorScheme.primary,

                            // ✅ Background
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // ✅ BALANCE
                    OutlinedTextField(
                        value = balance,
                        onValueChange = { balance = it },
                        label = { Text("Balance") },
                        colors = OutlinedTextFieldDefaults.colors(
                            // ✅ Text
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,

                            // ✅ Border
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),

                            // ✅ Label
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),

                            // ✅ Cursor
                            cursorColor = MaterialTheme.colorScheme.primary,

                            // ✅ Background
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // ✅ UPDATE BUTTON
                    Button(
                        onClick = {
                            val newBalance = balance.toDoubleOrNull()

                            if (newBalance != null && name.isNotEmpty()) {
                                viewModel.updateAccount(
                                    account.copy(name = name, balance = newBalance)
                                )
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary                        )
                    ) {
                        Text("Update")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // ✅ DELETE BUTTON
                    Button(
                        onClick = { showDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = dangerRed
                        )
                    ) {
                        Text("Delete")
                    }
                }
            }
        }

        // ✅ ✅ DIALOG PRO
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                containerColor = MaterialTheme.colorScheme.surface ,
                titleContentColor = MaterialTheme.colorScheme.onBackground,

                title = {
                    Text("Delete Account?")
                },

                text = {
                    Text(
                        text = "Are you sure you want to delete this account? This action cannot be undone.",
                        color = MaterialTheme.colorScheme.onBackground                    )
                },

                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteAccount(account)
                            showDialog = false
                            navController.popBackStack()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = dangerRed)
                    ) {
                        Text("Delete", color = MaterialTheme.colorScheme.onBackground)
                    }
                },

                dismissButton = {
                    OutlinedButton(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Cancel" , color = MaterialTheme.colorScheme.onBackground)
                    }
                }
            )
        }
    }
}