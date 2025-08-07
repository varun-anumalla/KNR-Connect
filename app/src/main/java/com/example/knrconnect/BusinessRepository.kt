package com.example.knrconnect

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

class BusinessRepository(private val dao: BusinessDao) {

    private val _businesses = MutableStateFlow<List<Business>>(emptyList())
    val businesses: Flow<List<Business>> = _businesses.asStateFlow()

    suspend fun initialize() {
        val initialList = dao.getAllBusinesses().first()
        _businesses.value = initialList
    }


    suspend fun refreshBusinesses(apiUrl: String) {
        try {
            // 1. Get the fresh list of businesses from the network.
            val networkBusinesses = RetrofitInstance.api.getBusinesses(apiUrl)

            // 2. Get the current list of ONLY your favorited businesses from the database.
            val favoritedBusinesses = dao.getFavoriteBusinesses().first()
            val favoriteNames = favoritedBusinesses.map { it.name }.toSet()

            // 3. Go through the new network list and apply the 'isFavorite' status from your local data.
            val updatedList = networkBusinesses.map { networkBusiness ->
                networkBusiness.copy(isFavorite = favoriteNames.contains(networkBusiness.name))
            }

            // 4. Save this newly merged list to the database.
            dao.insertAll(updatedList)

            // 5. Push the final, correct list to the UI.
            _businesses.value = dao.getAllBusinesses().first()

        } catch (e: Exception) {
            Log.e("KNR_FAVORITE_FIX", "Error refreshing businesses", e)
        }
    }

    fun getBusinessByName(name: String): Flow<Business?> = dao.getBusinessByName(name)

    suspend fun setFavorite(name: String, isFavorite: Boolean) {
        dao.setFavorite(name, isFavorite)
        // Refresh the list from the database to ensure the UI updates everywhere.
        _businesses.value = dao.getAllBusinesses().first()
    }

    fun getFavoriteBusinesses(): Flow<List<Business>> = dao.getFavoriteBusinesses()

    suspend fun clearFavorites() {
        dao.clearFavorites()
        _businesses.value = dao.getAllBusinesses().first()
    }
}