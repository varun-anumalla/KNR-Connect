package com.example.knrconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
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

data class NavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

            KNRConnectTheme(darkTheme = isDarkTheme, dynamicColor = false) {
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
        NavItem("home", "Home", Icons.Filled.Home, Icons.Outlined.Home),
        NavItem("favorites", "Favorites", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
        NavItem("settings", "Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
    )

    Scaffold(
        topBar = { TopBar() },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                navItems.forEach { item ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
                        selected = isSelected,
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
                DetailsScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
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
                SettingsScreen(
                    settingsViewModel = settingsViewModel,
                    themeViewModel = themeViewModel
                )
            }
        }
    }
}