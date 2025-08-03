package com.example.knrconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.knrconnect.ui.theme.KNRConnectTheme

class MainActivity : ComponentActivity() {

    private val repository = BusinessRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KNRConnectTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "main") {

                    // Main Screen Route
                    composable("main") {
                        val viewModel: MainViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return MainViewModel(repository) as T
                                }
                            }
                        )
                        MainScreen(
                            viewModel = viewModel,
                            onItemClick = {
                                navController.navigate("details/${it.name}/${it.category}")
                            }
                        )
                    }

                    // Details Screen Route
                    composable(
                        route = "details/{businessName}/{businessCategory}",
                    ) { backStackEntry ->
                        val businessName = backStackEntry.arguments?.getString("businessName") ?: ""

                        val viewModel: DetailsViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return DetailsViewModel(repository) as T
                                }
                            }
                        )

                        LaunchedEffect(key1 = Unit) {
                            viewModel.loadBusiness(businessName)
                        }

                        DetailsScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}