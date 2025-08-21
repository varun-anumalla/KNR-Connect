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

/**
 *
 * @property name The unique name of the business (Primary Key).
 * @property category The category the business belongs to (e.g., "Restaurant", "Hospital").
 * @property address The physical address of the business.
 * @property phone The contact phone number for the business.
 * @property mapLink A URL link to the business's location on Google Maps.
 * @property tags A list of hidden keywords used for searching (e.g., "biryani", "doctor" , "food").
 * @property isFavorite A boolean flag indicating if the user has marked this business as a favorite.
 */


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