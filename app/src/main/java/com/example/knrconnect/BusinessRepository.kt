package com.example.knrconnect

import kotlinx.coroutines.flow.Flow

class BusinessRepository(private val dao: BusinessDao) {

    fun getAllBusinesses(): Flow<List<Business>> {
        return dao.getAllBusinesses()
    }

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

    // this function calls the DAO to clear the favorites
    suspend fun clearFavorites() {
        dao.clearFavorites()
    }
}
