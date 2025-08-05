package com.example.knrconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.knrconnect.ui.theme.KNRConnectTheme

data class NavItem(val route: String, val icon: ImageVector, val label: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Create a single instance of the ThemeViewModel for the whole app
            val themeViewModel: ThemeViewModel = viewModel()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

            // Pass the theme state to our KNRConnectTheme
            KNRConnectTheme(darkTheme = isDarkTheme) {
                val database = AppDatabase.getInstance(applicationContext)
                val repository = BusinessRepository(database.businessDao())
                AppShell(repository = repository, themeViewModel = themeViewModel)
            }
        }
    }
}

@Composable
fun AppShell(repository: BusinessRepository, themeViewModel: ThemeViewModel) {
    val navController = rememberNavController()
    val navItems = listOf(
        NavItem("home", Icons.Default.Home, "Home"),
        NavItem("favorites", Icons.Default.Favorite, "Favorites"),
        NavItem("settings", Icons.Default.Settings, "Settings")
    )

    Scaffold(
        topBar = { TopBar() },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                navItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = "home",
            Modifier.padding(innerPadding)
        ) {
            composable("home") {
                val viewModel: MainViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return MainViewModel(repository) as T
                        }
                    }
                )
                MainScreen(
                    viewModel = viewModel,
                    onItemClick = { business ->
                        navController.navigate("details/${business.name}")
                    }
                )
            }

            composable("details/{businessName}") { backStackEntry ->
                val businessName = backStackEntry.arguments?.getString("businessName") ?: ""
                val viewModel: DetailsViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return DetailsViewModel(repository) as T
                        }
                    }
                )
                LaunchedEffect(key1 = businessName) {
                    viewModel.loadBusiness(businessName)
                }
                DetailsScreen(viewModel = viewModel)
            }

            composable("favorites") {
                val viewModel: FavoritesViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return FavoritesViewModel(repository) as T
                        }
                    }
                )
                FavoritesScreen(viewModel = viewModel, navController = navController)
            }

            composable("settings") {
                val settingsViewModel: SettingsViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return SettingsViewModel(repository) as T
                        }
                    }
                )
                // Pass both ViewModels to the SettingsScreen
                SettingsScreen(
                    settingsViewModel = settingsViewModel,
                    themeViewModel = themeViewModel
                )
            }
        }
    }
}