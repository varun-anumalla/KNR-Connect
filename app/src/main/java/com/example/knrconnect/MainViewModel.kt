package com.example.knrconnect

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val repository: BusinessRepository) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val businesses: StateFlow<List<Business>> = repository.businesses
        .onEach { businessList ->

            Log.d("KNR_DEBUG_VM", "Business list received from repository with size: ${businessList.size}")
        }
        .combine(searchQuery) { businessList, query ->
            if (query.isBlank()) {
                businessList
            } else {
                businessList.filter { business ->
                    business.name.contains(query, ignoreCase = true) ||
                            business.category.contains(query, ignoreCase = true) ||
                            business.tags.any { tag -> tag.contains(query, ignoreCase = true) }
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val apiUrl = "https://gist.githubusercontent.com/varun-anumalla/e8273cd857207fb1102811c05331eeb6/raw/3d267c11b46e7ce26bd1b9e440db07a907d1016a/knr-data.json"

    init {
        viewModelScope.launch {
            repository.initialize()
            fetchBusinesses()
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
    fun onClearSearchQuery() {
        _searchQuery.value = ""
    }

    private fun fetchBusinesses() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.refreshBusinesses(apiUrl)
            } catch (e: Exception) {
                _error.value = "Failed to load data."
            } finally {
                _isLoading.value = false
            }
        }
    }
}