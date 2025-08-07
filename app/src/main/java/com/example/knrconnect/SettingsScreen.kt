package com.example.knrconnect

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.knrconnect.BuildConfig

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    themeViewModel: ThemeViewModel
) {
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        // Dark Mode Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Dark Mode", style = MaterialTheme.typography.bodyLarge)
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { themeViewModel.toggleTheme() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        // Clear all Favorites Button
        Button(onClick = { settingsViewModel.clearFavorites() }) {
            Text("Clear All Favorites")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        // About Section
        Text(
            text = "About",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        // This will update automatically when we update the version in build.gradle.kts
        val version = BuildConfig.VERSION_NAME
        Text(text = "KNR Connect - Version $version")
        Text(text = "Developed by Varun Anumalla")
    }
}