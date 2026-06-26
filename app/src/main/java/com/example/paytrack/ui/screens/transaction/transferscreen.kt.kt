package com.example.paytrack.ui.screens.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.paytrack.data.localuser.SessionManager
import com.example.paytrack.ui.viewmodel.AccountViewModel


@Composable
fun TransferScreen(
    accountViewModel: AccountViewModel,
    navController: NavController ) {

    val accounts by accountViewModel.accounts.collectAsState()

    var fromId by remember { mutableStateOf<Long?>(null) }
    var toId by remember { mutableStateOf<Long?>(null) }
    var amount by remember { mutableStateOf("") }
    val context = LocalContext.current
    val session = SessionManager(context)
    val username = session.getUsername() ?: ""

    LaunchedEffect(Unit) {
        accountViewModel.loadAccounts(username)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {

        // ✅ BACK + TITLE
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

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
                text = "Transfer",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ✅ CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {

                // ✅ FROM
                DropdownAccount(
                    label = "From Account",
                    accounts = accounts,
                    onSelected = { fromId = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ✅ TO
                DropdownAccount(
                    label = "To Account",
                    accounts = accounts,
                    onSelected = { toId = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ✅ AMOUNT
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth() ,

                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground                ),

                keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    )
                    )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ✅ BUTTON + GRADIENT
        val context = LocalContext.current
        val session = SessionManager(context)
        Button(
            onClick = {
                val amt = amount.toDoubleOrNull()
                if (fromId != null && toId != null && amt != null) {
                    accountViewModel.transfer(fromId!!, toId!!, amt)

                    navController.navigate("home/$username") {
                        popUpTo("home/{username}") { inclusive = true }
                    }



                    amount = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary            ),
            contentPadding = PaddingValues()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF4A90E2),
                                Color(0xFF357ABD)
                            )
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Transfer",
                    color = MaterialTheme.colorScheme.onBackground ,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
