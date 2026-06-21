package com.example.paytrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paytrack.data.localuser.SessionManager
import com.example.paytrack.ui.viewmodel.UserViewModel

@Composable
fun LoginScreen(navController: NavController
                ,
                userViewModel: UserViewModel
) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf("") }
    var showSnackbar by remember { mutableStateOf(false) }

    val primaryBlue = Color(0xFF3B82F6)
    val background = Color(0xFFF8FAFC)

    val context = LocalContext.current
    val session = SessionManager(context)

    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            kotlinx.coroutines.delay(3000)
            showSnackbar = false
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ✅ ICON
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            tint = primaryBlue
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ TITLE
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ✅ CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // ✅ USERNAME
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },

                    leadingIcon = {
                        Icon(Icons.Default.Person, null, tint = primaryBlue)
                    },

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = primaryBlue,
                        focusedBorderColor = primaryBlue,
                        unfocusedBorderColor = Color.Gray
                    ),

                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ✅ PASSWORD
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },

                    visualTransformation =
                        if (passwordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),

                    leadingIcon = {
                        Icon(Icons.Default.Lock, null, tint = primaryBlue)
                    },

                    trailingIcon = {
                        IconButton(onClick = {
                            passwordVisible = !passwordVisible
                        }) {
                            Icon(
                                if (passwordVisible)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    },

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = primaryBlue,
                        focusedBorderColor = primaryBlue,
                        unfocusedBorderColor = Color.Gray
                    ),

                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ✅ BUTTON
        Button(
            onClick = {

                if (username.isBlank() || password.isBlank()) {
                    errorMessage = "Fill all fields ⚠️"
                    showSnackbar = true
                } else {

                    userViewModel.login(username, password) { success ->

                        if (success) {

                            session.saveLogin(username)

                            navController.navigate("home/$username") {
                                popUpTo("login") { inclusive = true }
                            }

                        } else {
                            errorMessage = "Invalid credentials ⚠️"
                            showSnackbar = true
                        }
                    }
            }
                      },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryBlue
            )
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Don't have an account?",
            color = Color.Gray
        )

        Text(
            "Sign Up",
            color = primaryBlue,
            modifier = Modifier.clickable {
                navController.navigate("signup")
            }
        )
    }

    // ✅ SNACKBAR
    if (showSnackbar) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            containerColor = Color.Black
        ) {
            Text(text = errorMessage, color = Color.White)
        }
    }
}