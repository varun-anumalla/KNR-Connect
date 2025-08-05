package com.example.knrconnect

import kotlinx.coroutines.flow.Flow

class BusinessRepository(private val dao: BusinessDao) {

    fun getAllBusinesses(): Flow<List<Business>> = dao.getAllBusinesses()

    suspend fun refreshBusinesses(apiUrl: String) {
        try {
            val networkBusinesses = RetrofitInstance.api.getBusinesses(apiUrl)
            dao.insertAll(networkBusinesses)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getBusinessByName(name: String): Flow<Business?> = dao.getBusinessByName(name)

    suspend fun setFavorite(name: String, isFavorite: Boolean) = dao.setFavorite(name, isFavorite)

    fun getFavoriteBusinesses(): Flow<List<Business>> = dao.getFavoriteBusinesses()

    suspend fun clearFavorites() = dao.clearFavorites()
}