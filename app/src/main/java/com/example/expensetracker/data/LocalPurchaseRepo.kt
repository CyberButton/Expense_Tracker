package com.example.expensetracker.data

import kotlinx.coroutines.flow.Flow

/*
local implementation of the repository
 */

class LocalPurchaseRepo(private val purchaseDao: PurchaseDao): PurchaseRepo {
    override fun getAllPurchasesStream(): Flow<List<Purchase>> {
        return purchaseDao.getAllPurchases()
    }

    override fun getPurchaseStream(id: Int): Flow<Purchase?> {
        return purchaseDao.getPurchase(id)
    }

    override suspend fun insertPurchase(purchase: Purchase) {
        purchaseDao.insert(purchase)
    }

    override suspend fun updatePurchase(purchase: Purchase) {
        purchaseDao.update(purchase)
    }

    override suspend fun deletePurchase(purchase: Purchase) {
        purchaseDao.delete(purchase)
    }
}