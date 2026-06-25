package com.example.paytrack.data.localtransaction

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val accountId: Long,

    val amount: Double,
    val type: String, // TRANSFER_IN / TRANSFER_OUT / SALE / VOID

    val fromAccountName: String?,
    val toAccountName: String?,

    val date: Long
)