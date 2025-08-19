package com.example.knrconnect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val repository: BusinessRepository) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val businesses: StateFlow<List<Business>> = repository.businesses
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

    private val apiUrl = "https://gist.githubusercontent.com/varun-anumalla/e8273cd857207fb1102811c05331eeb6/raw/75c35ec90932cbd526a9c4fac7b282d4a45bb1e9/knr-data.json"

    init {
        viewModelScope.launch {
            repository.initialize()
            fetchBusinesses(isInitialLoad = true)
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onClearSearchQuery() {
        _searchQuery.value = ""
    }

    // Public function for the UI to call
    fun refreshData() {
        fetchBusinesses(isInitialLoad = false)
    }

    private fun fetchBusinesses(isInitialLoad: Boolean) {
        viewModelScope.launch {
            if (isInitialLoad) {
                _isLoading.value = true
            } else {
                _isRefreshing.value = true
            }
            _error.value = null
            try {
                repository.refreshBusinesses(apiUrl)
            } catch (e: Exception) {
                _error.value = "Failed to load data."
            } finally {
                if (isInitialLoad) {
                    _isLoading.value = false
                } else {
                    _isRefreshing.value = false
                }
            }
        }
    }
}