package com.example.paytrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.paytrack.data.localuser.SessionManager
import com.example.paytrack.ui.viewmodel.UserViewModel
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.text.input.VisualTransformation


@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel
) {

    val context = LocalContext.current
    val session = SessionManager(context)
    val username = session.getUsername() ?: ""

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    var showOldPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }


    var message by remember { mutableStateOf("") }
    var showSnackbar by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            snackbarHostState.showSnackbar(message)
            showSnackbar = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .padding(padding)
        ) {

            // ✅ TOP (Back + Icon + Username)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back" ,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(Modifier.width(10.dp))

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User",
                    tint = MaterialTheme.colorScheme.onBackground
                )

                Spacer(Modifier.width(6.dp))

                Text(
                    text = username,
                    style = MaterialTheme.typography.titleMedium ,
                    color = MaterialTheme.colorScheme.onBackground                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // ✅ CARD (Passwords)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    OutlinedTextField(
                        value = oldPassword,
                        onValueChange = { oldPassword = it },
                        label = { Text("Old Password") },
                        singleLine = true,

                        visualTransformation =
                            if (showOldPassword) VisualTransformation.None
                            else PasswordVisualTransformation(),

                        trailingIcon = {
                            IconButton(onClick = {
                                showOldPassword = !showOldPassword
                            }) {
                                Icon(
                                    imageVector =
                                        if (showOldPassword) Icons.Default.Visibility
                                        else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle"
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,

                            focusedBorderColor = Color(0xFF4A90E2),
                            unfocusedBorderColor = Color(0xFF4A90E2),

                            focusedLabelColor = Color(0xFF4A90E2),
                            unfocusedLabelColor = Color(0xFF4A90E2),

                            cursorColor = Color(0xFF4A90E2)
                        ),


                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New Password") },
                        singleLine = true,

                        visualTransformation =
                            if (showNewPassword) VisualTransformation.None
                            else PasswordVisualTransformation(),

                        trailingIcon = {
                            IconButton(onClick = {
                                showNewPassword = !showNewPassword
                            }) {
                                Icon(
                                    imageVector =
                                        if (showNewPassword) Icons.Default.Visibility
                                        else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle"
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,

                            focusedBorderColor = Color(0xFF4A90E2),
                            unfocusedBorderColor = Color(0xFF4A90E2),

                            focusedLabelColor = Color(0xFF4A90E2),
                            unfocusedLabelColor = Color(0xFF4A90E2),

                            cursorColor = Color(0xFF4A90E2)
                        ),


                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))


            Button(
                onClick = {

                    if (oldPassword.isBlank() || newPassword.isBlank()) {
                        message = "⚠️ Fill all fields"
                        showSnackbar = true
                    } else {

                        userViewModel.changePassword(
                            username,
                            oldPassword,
                            newPassword
                        ) { success ->

                            if (success) {
                                message = "✅ Password updated"
                                showSnackbar = true
                                navController.popBackStack()
                            } else {
                                message = "❌ Wrong old password"
                                showSnackbar = true
                            }
                        }
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary                ),
                contentPadding = PaddingValues()
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF4A90E2),
                                    Color(0xFF357ABD)
                                )
                            ),
                            shape = RoundedCornerShape(50)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Change Password",
                        color = MaterialTheme.colorScheme.onBackground                    )
                }
            }
        }
    }
}