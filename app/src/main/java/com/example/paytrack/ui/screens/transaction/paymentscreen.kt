package com.example.paytrack.ui.screens.transaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.paytrack.ui.viewmodel.AccountViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import com.example.paytrack.data.localuser.SessionManager


@Composable
fun PaymentScreen(
    accountViewModel: AccountViewModel,
    navController: NavController
) {

    val accounts by accountViewModel.accounts.collectAsState()

    var accountId by remember { mutableStateOf<Long?>(null) }
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
                text = "Payment",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ✅ CARD (FORM)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                // ✅ ACCOUNT DROPDOWN
                DropdownAccount(
                    label = "Choose Account",
                    accounts = accounts,
                    onSelected = { accountId = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ✅ AMOUNT FIELD
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },

                    textStyle = TextStyle(
                        color = Color.Black
                    ),

                modifier = Modifier.fillMaxWidth() ,

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal )

                    )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ✅ BUTTON (GRADIENT)
        val context = LocalContext.current
        val session = SessionManager(context)
        Button(
            onClick = {
                val amt = amount.toDoubleOrNull()
                if (accountId != null && amt != null) {
                    accountViewModel.pay(accountId!!, amt)


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
                containerColor = Color.Transparent
            ),
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
                    text = "Pay",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}