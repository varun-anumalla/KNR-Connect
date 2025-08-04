package com.example.knrconnect

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun FavoritesScreen(viewModel: FavoritesViewModel, navController: NavController) {
    val favoriteBusinesses by viewModel.favoriteBusinesses.collectAsState()

    if (favoriteBusinesses.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "You haven't favorited any businesses yet.")
        }
    } else {
        BusinessList(
            businesses = favoriteBusinesses,
            onItemClick = { business ->
                navController.navigate("details/${business.name}")
            }
        )
    }
}
