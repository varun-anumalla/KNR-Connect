package com.example.knrconnect

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = { Text(text = "KNR Connect") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
fun BusinessList(businesses: List<Business>, onItemClick: (Business) -> Unit) {
    LazyColumn {
        items(businesses) { business ->
            BusinessListItem(
                business = business,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
fun BusinessListItem(business: Business, onItemClick: (Business) -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onItemClick(business) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = business.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = business.category,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}