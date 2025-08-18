package com.example.knrconnect

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return value.split(",").map { it.trim() }
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(",")
    }
}

@Entity(tableName = "businesses")
@TypeConverters(Converters::class)
data class Business(
    @PrimaryKey val name: String,
    val category: String,
    val description: String,
    val imageUrls: List<String>,
    val address: String,
    val phone: String,
    val mapLink: String,
    val tags: List<String>, //  list of hidden keywords
    var isFavorite: Boolean = false
)