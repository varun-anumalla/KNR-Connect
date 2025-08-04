package com.example.knrconnect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repository: BusinessRepository
) : ViewModel() {

    private val _business = MutableStateFlow<Business?>(null)
    val business: StateFlow<Business?> = _business.asStateFlow()

    fun loadBusiness(name: String) {
        viewModelScope.launch {
            repository.getBusinessByName(name).collect { businessFromDb ->
                _business.value = businessFromDb
            }
        }
    }

    fun toggleFavorite() {
        _business.value?.let { currentBusiness ->
            viewModelScope.launch {
                repository.setFavorite(currentBusiness.name, !currentBusiness.isFavorite)
            }
        }
    }
}