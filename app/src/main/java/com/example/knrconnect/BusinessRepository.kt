package com.example.knrconnect

import kotlinx.coroutines.flow.Flow
class BusinessRepository(private val dao: BusinessDao) {
    suspend fun clearFavorites() {
        dao.run { clearFavorites() }
    }

    fun getAllBusinesses(): Flow<List<Business>> {
        return dao.getAllBusinesses()
    }
    // Fetches fresh data from the network and saves it to the local database
    suspend fun refreshBusinesses(apiUrl: String) {
        try {
            val networkBusinesses = RetrofitInstance.api.getBusinesses(apiUrl)
            dao.insertAll(networkBusinesses)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getBusinessByName(name: String): Flow<Business?> {
        return dao.getBusinessByName(name)
    }

    suspend fun setFavorite(name: String, isFavorite: Boolean) {
        dao.setFavorite(name, isFavorite)
    }

    fun getFavoriteBusinesses(): Flow<List<Business>> {
        return dao.getFavoriteBusinesses()
    }
}