package com.example.knrconnect

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val repository: BusinessRepository) : ViewModel() {

    val businesses: StateFlow<List<Business>> = repository.getAllBusinesses()
        .onEach { businessList ->
            Log.d("DATA_FLOW", "UI is observing ${businessList.size} items from database")
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val apiUrl = "https://gist.githubusercontent.com/varun-anumalla/e8273cd857207fb1102811c05331eeb6/raw/c0bc838c6e7980c5699398eb47222fbe7c1c7c39/knr-data.json"
    init {
        fetchBusinesses()
    }

    private fun fetchBusinesses() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = repository.refreshBusinesses(apiUrl)
                Log.d("FINAL_CHECK", "The network returned a list with ${result.size} items.")
            } catch (e: Exception) {
                _error.value = "Failed to load data. Check connection."
            }
            _isLoading.value = false
        }
    }
}