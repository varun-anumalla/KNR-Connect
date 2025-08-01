package com.example.knrconnect

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Business(val name: String, val category: String)

class MainViewModel : ViewModel() {

    private val _businesses = MutableStateFlow<List<Business>>(emptyList())
    val businesses = _businesses.asStateFlow()

    init {

        val dummyList = listOf(
            Business(name = "Swagath Restaurant", category = "Food"),
            Business(name = "Apollo Clinic", category = "Health"),
            Business(name = "Raju's Bike Repair", category = "Service"),
            Business(name = "Prathima Multiplex", category = "Entertainment"),
            Business(name = "Book World", category = "Shopping"),
            Business(name = "City Tailors", category = "Service"),
            Business(name = "Aditya Medical Hall", category = "Health"),
            Business(name = "Deccan Fast Food", category = "Food"),
            Business(name = "The Computer Store", category = "Shopping"),
            Business(name = "Royal Car Wash", category = "Service")
        )
        _businesses.value = dummyList
    }
}