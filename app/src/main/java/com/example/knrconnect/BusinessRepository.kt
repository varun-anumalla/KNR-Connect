package com.example.knrconnect

import kotlinx.coroutines.flow.Flow

class BusinessRepository(private val dao: BusinessDao) {

    fun getAllBusinesses(): Flow<List<Business>> {
        return dao.getAllBusinesses()
    }

    suspend fun refreshBusinesses(apiUrl: String): List<Business> {
        var networkBusinesses: List<Business> = emptyList()
        try {
            networkBusinesses = RetrofitInstance.api.getBusinesses(apiUrl)
            dao.insertAll(networkBusinesses)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return networkBusinesses
    }
    fun getBusinessByName(name: String): Flow<Business?> {
        return dao.getBusinessByName(name)
    }

    suspend fun setFavorite(name: String, isFavorite: Boolean) {
        dao.setFavorite(name, isFavorite)
    }
}