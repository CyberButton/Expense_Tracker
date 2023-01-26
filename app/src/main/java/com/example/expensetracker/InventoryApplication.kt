
package com.example.expensetracker

import android.app.Application
import com.example.expensetracker.data.AppContainer
import com.example.expensetracker.data.AppDataContainer

class InventoryApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
