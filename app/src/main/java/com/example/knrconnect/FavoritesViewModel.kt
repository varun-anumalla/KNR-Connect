
package com.example.knrconnect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * The ViewModel for the FavoritesScreen.
 * Its single responsibility is to provide a real-time list of businesses
 * that the user has marked as a favorite.
 *
 * @property repository The single source of truth for all business data.
 */

class FavoritesViewModel(repository: BusinessRepository) : ViewModel() {

    /**
     * A StateFlow that emits the current list of favorite businesses from the database.
     * The UI observes this flow to display the list.
     */


    val favoriteBusinesses: StateFlow<List<Business>> = repository.getFavoriteBusinesses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}