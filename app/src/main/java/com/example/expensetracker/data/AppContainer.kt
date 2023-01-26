package com.example.expensetracker.data

import android.content.Context
/*
App container for dependency injection
 */
interface AppContainer {
    val purchaseRepo: PurchaseRepo
}

/*
implementation that provides purchaseRepo
 */
class AppDataContainer(private val context: Context) : AppContainer {
    override val purchaseRepo: PurchaseRepo by lazy {
        LocalPurchaseRepo(PurchasesDatabse.getDatabase(context).purchaseDao())
    }

}