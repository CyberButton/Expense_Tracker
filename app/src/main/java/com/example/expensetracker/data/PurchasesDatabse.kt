package com.example.expensetracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/*
The actual database class with single instance
 */

@Database(entities = [Purchase::class], version = 3, exportSchema = false)
abstract class PurchasesDatabse : RoomDatabase() {

abstract fun purchaseDao(): PurchaseDao

companion object {
    // instance = to have reference to this class
    @Volatile
    private var Instance: PurchasesDatabse? = null

    fun getDatabase(context: Context): PurchasesDatabse {
        return Instance ?: synchronized(this) {
            Room.databaseBuilder(context, PurchasesDatabse::class.java, "purchase_db")
                .fallbackToDestructiveMigration()
                .build()
                .also { Instance = it }
        }
    }
}

}