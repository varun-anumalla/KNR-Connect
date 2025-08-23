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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
@OptIn(ExperimentalMaterial3Api::class)
/**
 * A reusable TopAppBar composable for the main screens of the app.
 */
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
/**
 * A reusable LazyColumn for displaying a list of businesses.
 *
 * @param businesses The list of [Business] objects to display.
 * @param onItemClick A lambda function to be invoked when a business item is clicked.
 */
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
/**
 * A reusable Card composable that displays a single business item in a list.
 *
 * @param business The [Business] object to display.
 * @param onItemClick A lambda function to be invoked when the card is clicked.
 */
@Composable
fun BusinessListItem(business: Business, onItemClick: (Business) -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onItemClick(business) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Storefront,
                contentDescription = "Business Category",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = business.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = business.category,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}