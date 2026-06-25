package com.example.paytrack.data.localtransaction

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert
    suspend fun insert(transaction: Transaction)

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE accountId = :id ORDER BY date DESC")
    fun getTransactionsByAccount(id: Long): Flow<List<Transaction>>
    @Delete
    suspend fun delete(transaction: Transaction)

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()

    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'PAYMENT' AND date >= :startOfMonth")
    suspend fun getMonthlyPayments(startOfMonth: Long): Double

}
