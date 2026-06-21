package com.example.paytrack.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paytrack.ui.viewmodel.UserViewModel

@Composable
fun SignUpScreen(navController: NavController,
                 userViewModel: UserViewModel
) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf("") }
    var showSnackbar by remember { mutableStateOf(false) }

    val primaryBlue = Color(0xFF3B82F6)
    val background = Color(0xFFF8FAFC)


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
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            tint = primaryBlue
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ TITLE
        Text(
            text = "Sign Up",
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

                Spacer(modifier = Modifier.height(12.dp))

                // ✅ CONFIRM PASSWORD
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },

                    visualTransformation =
                        if (confirmPasswordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),

                    leadingIcon = {
                        Icon(Icons.Default.Lock, null, tint = primaryBlue)
                    },

                    trailingIcon = {
                        IconButton(onClick = {
                            confirmPasswordVisible = !confirmPasswordVisible
                        }) {
                            Icon(
                                if (confirmPasswordVisible)
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

                if (username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                    errorMessage = "Fill all fields ⚠️"
                    showSnackbar = true
                } else if (password != confirmPassword) {
                    errorMessage = "Passwords do not match ⚠️"
                    showSnackbar = true
                } else {

                    userViewModel.register(username, password) { success ->
                        if (success) {
                            navController.navigate("login")
                        } else {
                            errorMessage = "User already exists ⚠️"
                            showSnackbar = true
                        }
                    }

                }
            }
        ) {
            Text("Sign Up")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Already have an account?",
            color = Color.Gray
        )

        Text(
            text = "Login",
            color = primaryBlue,
            modifier = Modifier.clickable {
                navController.navigate("login")
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