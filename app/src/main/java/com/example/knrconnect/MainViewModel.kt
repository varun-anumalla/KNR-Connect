package com.example.knrconnect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
/**
 * The ViewModel for the MainScreen.
 * It is responsible for fetching the list of all businesses, handling search queries,
 * and managing UI states like loading, refreshing, and errors.
 *
 * @property repository The single source of truth for all business data.
 */
class MainViewModel(private val repository: BusinessRepository) : ViewModel() {
    /**
     * Represents the initial loading state when the app first starts.
     */
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    /**
     * Represents the loading state for the pull-to-refresh action.
     */
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    /**
     * Holds a potential error message if the data fetch fails. Null if there is no error.
     */
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    /**
     * Holds the current text entered by the user in the search bar.
     */
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    /**
     * The final list of businesses to be displayed on the UI.
     * This flow combines the full list from the repository with the current search query
     * to produce a filtered list in real-time.
     */
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

    private val apiUrl = "https://gist.githubusercontent.com/varun-anumalla/e8273cd857207fb1102811c05331eeb6/raw/10ac75a4700c1e5188bc10e0913784ecd7a95634/knr-data.json"

    init {
        viewModelScope.launch {
            repository.initialize()
            fetchBusinesses(isInitialLoad = true)
        }
    }
    /**
     * Called when the user types in the search bar.
     * @param query The new text from the search bar.
     */
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
    /**
     * Called when the user taps the 'X' button in the search bar.
     */
    fun onClearSearchQuery() {
        _searchQuery.value = ""
    }

    /**
     * Public function for the UI to trigger a data refresh (e.g., from pull-to-refresh).
     */

    fun refreshData() {
        fetchBusinesses(isInitialLoad = false)
    }
    /**
     * Fetches the latest business list from the repository and updates the UI state.
     * @param isInitialLoad Differentiates between the first load (shows a centered spinner)
     * and a subsequent refresh (shows the pull-to-refresh indicator).
     */
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