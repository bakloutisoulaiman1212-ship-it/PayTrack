package com.example.paytrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.paytrack.data.localuser.SessionManager
import com.example.paytrack.ui.viewmodel.NotificationViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    username: String,
    notificationViewModel: NotificationViewModel,
    darkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val session = SessionManager(context)
    val unreadCount by notificationViewModel.unreadCount.collectAsState(initial = 0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                    text = "👋 Welcome",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = username,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ThemeToggleSwitch(darkMode = darkMode, onToggle = onToggleDarkMode)

                BadgedBox(
                    badge = {
                        if (unreadCount > 0) {
                            Badge { Text("$unreadCount") }
                        }
                    }
                ) {
                    IconButton(onClick = { navController.navigate("notifications") }) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                IconButton(onClick = { navController.navigate("profile") }) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                IconButton(onClick = {
                    session.logout()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ✅ Grid يملأ باقي الشاشة
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ✅ ROW 1
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HomeCard(
                    emoji = "📁",
                    label = "Accounts",
                    modifier = Modifier.weight(1f).fillMaxHeight()
                ) { navController.navigate("list") }

                HomeCard(
                    emoji = "📊",
                    label = "Dashboard",
                    modifier = Modifier.weight(1f).fillMaxHeight()
                ) { navController.navigate("dashboard") }
            }

            // ✅ ROW 2
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HomeCard(
                    emoji = "🔁",
                    label = "Transfer",
                    modifier = Modifier.weight(1f).fillMaxHeight()
                ) { navController.navigate("transfer") }

                HomeCard(
                    emoji = "💳",
                    label = "Payment",
                    modifier = Modifier.weight(1f).fillMaxHeight()
                ) { navController.navigate("payment") }
            }

            // ✅ ROW 3 — History في الوسط
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.Center
            ) {
                HomeCard(
                    emoji = "📜",
                    label = "History",
                    modifier = Modifier.fillMaxWidth(0.5f).fillMaxHeight()
                ) { navController.navigate("history") }
            }
        }
    }
}

@Composable
fun HomeCard(
    emoji: String,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
fun ThemeToggleSwitch(
    darkMode: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .width(60.dp)
            .height(30.dp)
            .background(
                color = if (darkMode) Color.DarkGray else Color.LightGray,
                shape = RoundedCornerShape(50)
            )
            .clickable { onToggle(!darkMode) }
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.LightMode, null, tint = Color.Yellow, modifier = Modifier.size(16.dp))
            Icon(Icons.Default.DarkMode, null, tint = Color.White, modifier = Modifier.size(16.dp))
        }
        Box(
            modifier = Modifier
                .size(22.dp)
                .align(if (darkMode) Alignment.CenterEnd else Alignment.CenterStart)
                .background(Color.White, CircleShape)
        )
    }
}