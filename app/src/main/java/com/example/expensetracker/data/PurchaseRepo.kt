package com.example.expensetracker.data

import kotlinx.coroutines.flow.Flow

/*
repository to obey internal architecture
best practices
 */

interface PurchaseRepo {

    fun getAllPurchasesStream(): Flow<List<Purchase>>

    fun getPurchaseStream(id: Int): Flow<Purchase?>

    suspend fun insertPurchase(purchase: Purchase)

    suspend fun updatePurchase(purchase: Purchase)

    suspend fun deletePurchase(purchase: Purchase)

}