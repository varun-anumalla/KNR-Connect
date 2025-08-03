package com.example.knrconnect

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
 import androidx.compose.foundation.clickable

@Composable
fun BusinessListItem(business: Business, onItemClick: (Business) -> Unit) {
    Card(
        modifier = Modifier
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .fillMaxWidth()
            .clickable { onItemClick(business) }
    ) {
       Column(
           modifier= Modifier.padding(16.dp)
       ){
           Text(
               text= business.name,
               style= MaterialTheme.typography.titleMedium
           )
           Text(
               text= business.category,
               style= MaterialTheme.typography.bodySmall
           )
       }
    }
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
fun MainScreen(viewModel: MainViewModel, onItemClick: (Business) -> Unit) {
    val businesses by viewModel.businesses.collectAsState()
    Scaffold(
        topBar = { TopBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            BusinessList(
                businesses = businesses,
                onItemClick = onItemClick
            )
        }
    }
}