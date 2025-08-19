package com.example.knrconnect

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

class BusinessRepository(private val dao: BusinessDao) {

    private val _businesses = MutableStateFlow<List<Business>>(emptyList())
    val businesses: Flow<List<Business>> = _businesses.asStateFlow()


     // Initializes the repository by loading data from the database.

    suspend fun initialize() {
        // Loads the initial state from the database, which might be empty on first launch.
        _businesses.value = dao.getAllBusinesses().first()
    }

    /**
     * This function is the single point of truth for refreshing data.
     * It handles network fetches, favorite preservation, and error propagation.
     */
    suspend fun refreshBusinesses(apiUrl: String) {
        try {
            // 1. Get the fresh list from the network.
            val networkBusinesses = RetrofitInstance.api.getBusinesses(apiUrl)

            // 2. Get the current list of ONLY your favorited businesses from the database.
            val favoritedBusinesses = dao.getFavoriteBusinesses().first()
            val favoriteNames = favoritedBusinesses.map { it.name }.toSet()

            // 3. Merge the network data with the local favorite status.
            val updatedList = networkBusinesses.map { networkBusiness ->
                networkBusiness.copy(isFavorite = favoriteNames.contains(networkBusiness.name))
            }

            // 4. Save the newly merged list to the database.
            dao.insertAll(updatedList)

            // 5. After a successful refresh, push the new list to the UI.
            _businesses.value = dao.getAllBusinesses().first()

        } catch (e: Exception) {
            Log.e("BusinessRepository", "Error refreshing businesses", e)
            // If the network fails, re-throw the exception so the ViewModel knows about it.
            throw e
        }
    }


    fun getBusinessByName(name: String): Flow<Business?> = dao.getBusinessByName(name)

    suspend fun setFavorite(name: String, isFavorite: Boolean) {
        dao.setFavorite(name, isFavorite)
        _businesses.value = dao.getAllBusinesses().first()
    }

    fun getFavoriteBusinesses(): Flow<List<Business>> = dao.getFavoriteBusinesses()

    suspend fun clearFavorites() {
        dao.clearFavorites()
        _businesses.value = dao.getAllBusinesses().first()
    }
}