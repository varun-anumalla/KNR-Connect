package com.example.knrconnect

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(businesses: List<Business>)

    @Query("SELECT * FROM businesses")
    fun getAllBusinesses(): Flow<List<Business>>

    @Query("SELECT * FROM businesses WHERE name = :name")
    fun getBusinessByName(name: String): Flow<Business?>

    @Query("UPDATE businesses SET isFavorite = :isFavorite WHERE name = :name")
    suspend fun setFavorite(name: String, isFavorite: Boolean)

    @Query("SELECT * FROM businesses WHERE isFavorite = 1")
    fun getFavoriteBusinesses(): Flow<List<Business>>

    @Query("UPDATE businesses SET isFavorite = 0 WHERE isFavorite = 1")
    suspend fun clearFavorites()
}

