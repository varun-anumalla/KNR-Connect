package com.example.knrconnect

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * A simple ViewModel responsible for managing the theme state of the application.
 * It allows the user to toggle between light and dark mode.
 */

class ThemeViewModel : ViewModel() {

    /**
     * A private, mutable state flow that holds the current theme state.
     * `false` represents Light Mode, `true` represents Dark Mode.
     */
    private val _isDarkTheme = MutableStateFlow(false) // Default light theme

    /**
     * A public, read-only StateFlow that the UI can observe to react to theme changes.
     */

    val isDarkTheme = _isDarkTheme.asStateFlow()

    fun toggleTheme() {
        /**
         * Toggles the current theme state between light and dark mode.
         */
        _isDarkTheme.value = !_isDarkTheme.value
    }
}
