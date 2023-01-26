package com.example.expensetracker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/*
Database access objet to access Purchase database
 */

@Dao
interface PurchaseDao {

@Insert(onConflict = OnConflictStrategy.IGNORE)
suspend fun insert(purchase: Purchase)

@Update
suspend fun update(purchase: Purchase)

@Delete
suspend fun delete(purchase: Purchase)

@Query("SELECT * FROM Purchase WHERE id = :id")
fun getPurchase(id: Int): Flow<Purchase>

@Query("SELECT * FROM Purchase ORDER BY date")
fun getAllPurchases(): Flow<List<Purchase>>
}