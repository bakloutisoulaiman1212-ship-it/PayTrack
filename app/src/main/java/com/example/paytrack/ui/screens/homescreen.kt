package com.example.paytrack.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.paytrack.data.localuser.SessionManager


@Composable
fun HomeScreen(navController: NavController,username: String) {

    val primaryBlue = Color(0xFF3B82F6)
    val background = Color(0xFFF8FAFC)
    val context = LocalContext.current
    val session = SessionManager(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(16.dp)
    ) {

        // ✅ TOP BAR
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Text(
                    text = "\uD83D\uDC4B Welcome",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )

                Text(
                    text = username,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // ✅ Profile Icon
                IconButton(onClick = {
                    navController.navigate("profile")
                }) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile"
                    )
                }

                val context = LocalContext.current
                val session = SessionManager(context)
                // ✅ Logout Icon
                IconButton(onClick = {
                    session.logout()

                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }

                }) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ✅ ACCOUNTS CARD
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate("list")
                },
            colors = CardDefaults.cardColors(
                containerColor = primaryBlue
            ),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {

            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("📁", fontSize = 22.sp)

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Accounts",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ DASHBOARD CARD
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate("dashboard")
                },
            colors = CardDefaults.cardColors(
                containerColor = primaryBlue
            ),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {

            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("📊", fontSize = 22.sp)

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Dashboard",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // ✅ TRANSFER CARD
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate("transfer")
                },
            colors = CardDefaults.cardColors(
                containerColor = primaryBlue
            ),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🔁", fontSize = 22.sp)

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Transfer",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ PAYMENT CARD
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate("payment")
                },
            colors = CardDefaults.cardColors(
                containerColor = primaryBlue
            ),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("💳", fontSize = 22.sp)

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Payment",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ HISTORY CARD
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate("history")
                },
            colors = CardDefaults.cardColors(
                containerColor = primaryBlue
            ),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("📜", fontSize = 22.sp)

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "History",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
