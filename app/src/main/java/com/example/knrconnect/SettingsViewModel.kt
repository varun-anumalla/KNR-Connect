
package com.example.knrconnect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: BusinessRepository) : ViewModel() {
    fun clearFavorites() {
        viewModelScope.launch {
            repository.clearFavorites()
        }
    }
}
