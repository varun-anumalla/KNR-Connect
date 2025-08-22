package com.example.knrconnect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * The ViewModel for the DetailsScreen.
 * It is responsible for fetching a specific business's details and handling user actions
 * like toggling the favorite status.
 *
 * @property repository The single source of truth for business data.
 */
class DetailsViewModel(
    private val repository: BusinessRepository
) : ViewModel() {

    /**
     * A private, mutable state flow that holds the details of the currently displayed business.
     */
    private val _business = MutableStateFlow<Business?>(null)

    /**
     * The public, read-only StateFlow that the DetailsScreen observes to get business details.
     */
    val business: StateFlow<Business?> = _business.asStateFlow()

    /**
     * Loads the details for a specific business from the repository based on its name.
     * @param name The name of the business to load.
     */
    fun loadBusiness(name: String) {
        viewModelScope.launch {
            repository.getBusinessByName(name).collect { businessFromDb ->
                _business.value = businessFromDb
            }
        }
    }

    /**
     * Toggles the favorite status of the current business.
     */
    fun toggleFavorite() {
        _business.value?.let { currentBusiness ->
            viewModelScope.launch {
                repository.setFavorite(currentBusiness.name, !currentBusiness.isFavorite)
            }
        }
    }
}