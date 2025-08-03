package com.example.knrconnect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(private val repository: BusinessRepository) : ViewModel() {

    private val _business = MutableStateFlow<Business?>(null)
    val business = _business.asStateFlow()

    fun loadBusiness(name: String) {
        viewModelScope.launch {
            _business.value = repository.getBusinessByName(name)
        }
    }
}