
package com.example.knrconnect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * The ViewModel for the SettingsScreen.
 * It handles the logic for user actions taken on the settings page,
 * such as clearing all favorites.
 *
 * @property repository The single source of truth for all business data.
 */

class SettingsViewModel(private val repository: BusinessRepository) : ViewModel() {
    /**
     * Deletes all favorites from the local database by calling the repository.
     * This is executed in a coroutine on a background thread.
     */
    fun clearFavorites() {
        viewModelScope.launch {
            repository.clearFavorites()
        }
    }
}
