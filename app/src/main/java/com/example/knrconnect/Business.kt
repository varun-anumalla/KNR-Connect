package com.example.knrconnect

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "businesses")
data class Business(
    @PrimaryKey val name: String,
    val category: String,
    val address: String,
    val mapLink: String,
    var isFavorite: Boolean = false
)