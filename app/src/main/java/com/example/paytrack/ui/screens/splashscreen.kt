package com.example.paytrack.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.paytrack.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    LaunchedEffect(Unit) {
        delay(2000)

        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B1F3A)), // ✅ Dark Blue background
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ✅ LOGO
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(180.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ✅ TEXT تحت اللوغو
        Text(
            text = "SMART PAYMENT MANAGER",
            fontSize = 15.sp,
            letterSpacing = 2.sp,
            color = Color(0xFF90CAF9), // ✅ Light Blue
            textAlign = TextAlign.Center
        )
    }
}