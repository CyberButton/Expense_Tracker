package com.example.expensetracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey



/*
An Entity class defines a table,
and each instance of this class
represents a row in the database table.
*/

@Entity
data class Purchase (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val price: Int,
    val date: String,
    val time: String
    )