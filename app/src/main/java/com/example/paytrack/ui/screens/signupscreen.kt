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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import com.example.paytrack.ui.viewmodel.UserViewModel

@Composable
fun SignUpScreen(
    navController: NavController,
    userViewModel: UserViewModel
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showSnackbar by remember { mutableStateOf(false) }

    val blue = MaterialTheme.colorScheme.primary
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onBackground = MaterialTheme.colorScheme.onBackground

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ✅ ICON
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            tint = onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ TITLE
        Text(
            text = "Sign Up",
            style = MaterialTheme.typography.headlineMedium,
            color = onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ✅ CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // ✅ USERNAME
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, null, tint = onSurface)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = onSurface,
                        unfocusedTextColor = onSurface,
                        cursorColor = blue,
                        focusedBorderColor = blue,
                        unfocusedBorderColor = onSurface.copy(alpha = 0.4f),
                        focusedLabelColor = blue,
                        unfocusedLabelColor = onSurface.copy(alpha = 0.6f),
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    )
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
                        Icon(Icons.Default.Lock, null, tint = onSurface)
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                if (passwordVisible) Icons.Default.Visibility
                                else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = onSurface
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = onSurface,
                        unfocusedTextColor = onSurface,
                        cursorColor = blue,
                        focusedBorderColor = blue,
                        unfocusedBorderColor = onSurface.copy(alpha = 0.4f),
                        focusedLabelColor = blue,
                        unfocusedLabelColor = onSurface.copy(alpha = 0.6f),
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    )
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
                        Icon(Icons.Default.Lock, null, tint = onSurface)
                    },
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                if (confirmPasswordVisible) Icons.Default.Visibility
                                else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = onSurface
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = onSurface,
                        unfocusedTextColor = onSurface,
                        cursorColor = blue,
                        focusedBorderColor = blue,
                        unfocusedBorderColor = onSurface.copy(alpha = 0.4f),
                        focusedLabelColor = blue,
                        unfocusedLabelColor = onSurface.copy(alpha = 0.6f),
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    )
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
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = blue
            )
        ) {
            Text("Sign Up", color = MaterialTheme.colorScheme.onPrimary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Already have an account?",
            color = onBackground
        )

        Text(
            text = "Login",
            color = blue,
            modifier = Modifier.clickable {
                navController.navigate("login")
            }
        )
    }

    // ✅ SNACKBAR
    if (showSnackbar) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            containerColor = MaterialTheme.colorScheme.errorContainer
        ) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.onErrorContainer)
        }
    }
}