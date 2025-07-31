package com.example.knrconnect

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults


@Composable
fun BusinessListItem(businessName:String, category: String ) {
    Card(
        modifier = Modifier
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .fillMaxWidth()
    ) {
       Column(
           modifier= Modifier.padding(16.dp)
       ){
           Text(
               text= businessName,
               style= MaterialTheme.typography.titleMedium
           )
           Text(
               text= category,
               style= MaterialTheme.typography.bodySmall
           )
       }
    }
}
@Preview(showBackground = true)
@Composable
fun BusinessList() {
    LazyColumn {
        items(30) { index ->
            BusinessListItem(
                businessName = "Business #${index + 1}",
                category = "Category type ${index % 3 + 1}"
            )
        }
    }
}
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
fun MainScreen() {
    Scaffold(
        topBar = { TopBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            BusinessList()
        }
    }
}