package com.example.knrconnect

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

    private val _allBusinesses = repository.getAllBusinesses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val businesses: StateFlow<List<Business>> = _searchQuery
        .combine(_allBusinesses) { query, businessList ->
            if (query.isBlank()) {
                businessList
            } else {
                businessList.filter { business ->
                    val doesQueryMatch = business.name.contains(query, ignoreCase = true) ||
                            business.category.contains(query, ignoreCase = true) ||
                            business.tags.any { tag -> tag.contains(query, ignoreCase = true) }
                    doesQueryMatch
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


        private val apiUrl =  "https://gist.githubusercontent.com/varun-anumalla/e8273cd857207fb1102811c05331eeb6/raw/328ff06d9cbb3fc8ea3466ae268c7dd9161ce5ff/knr-data.json"

    init {
        fetchBusinesses()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    // Triggers the  network request to refresh the local database
    private fun fetchBusinesses() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.refreshBusinesses(apiUrl)
            } catch (e: Exception) {
                _error.value = "Failed to load data. Check connection."
            }
            _isLoading.value = false
        }
    }
}

