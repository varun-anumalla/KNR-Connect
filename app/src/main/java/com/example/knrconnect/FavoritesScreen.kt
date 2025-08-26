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


/**
 * A composable screen that displays a list of businesses the user has marked as their favorite.
 *
 * This screen observes the list of favorite businesses from the [FavoritesViewModel].
 * It displays a message if the list is empty, otherwise, it shows the businesses
 * using the reusable [BusinessList] composable.
 *
 * @param viewModel The ViewModel responsible for providing the list of favorite businesses.
 * @param navController The navigation controller used to navigate to the details screen
 * when a business is clicked.
 */
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

