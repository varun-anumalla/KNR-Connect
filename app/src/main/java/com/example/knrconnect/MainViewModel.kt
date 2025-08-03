package com.example.knrconnect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MainViewModel(private val repository: BusinessRepository) : ViewModel() {

    private val _businesses = MutableStateFlow<List<Business>>(emptyList())
    val businesses = _businesses.asStateFlow()

    private val apiUrl = "https://gist.githubusercontent.com/varun-anumalla/e8273cd857207fb1102811c05331eeb6/raw/b426a13e3e9cfaeda9cc66387cbf4379ac3a72c6/knr-data.json"

    init {
        fetchBusinesses()
    }

    private fun fetchBusinesses() {
        viewModelScope.launch {
            try {
                _businesses.value = repository.getBusinesses(apiUrl)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}