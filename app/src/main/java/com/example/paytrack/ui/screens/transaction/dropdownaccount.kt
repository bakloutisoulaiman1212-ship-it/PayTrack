package com.example.paytrack.ui.screens.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.paytrack.data.localaccount.Account
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownAccount(
    label: String,
    accounts: List<Account>,
    onSelected: (Long) -> Unit
) {

    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val filteredAccounts = accounts.filter {
        it.name.contains(query, ignoreCase = true)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {

        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                expanded = true
            },
            label = { Text(label) },

            textStyle = TextStyle(color = Color.Black),

        modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),

            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false } ,

            modifier = Modifier
                .background(Color.White)

        ) {

            if (filteredAccounts.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No result" ,color = Color.Black ) },
                    onClick = {}
                )
            }

            filteredAccounts.forEach { account ->
                DropdownMenuItem(
                    text = { Text(account.name, color = Color.Black) },
                    onClick = {
                        query = account.name
                        onSelected(account.id)
                        expanded = false
                    }
                )
            }
        }
    }
}