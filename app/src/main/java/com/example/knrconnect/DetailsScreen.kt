package com.example.knrconnect

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.knrconnect.ui.theme.Green
import com.valentinilk.shimmer.shimmer
import androidx.compose.foundation.background
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import coil.compose.AsyncImagePainter
import com.example.knrconnect.ui.theme.MidGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(viewModel: DetailsViewModel, onNavigateBack: () -> Unit) {
    val business by viewModel.business.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* No title */ },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        business?.let { b ->
            // A regular Column that is scrollable
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()) // This makes the whole screen scrollable
            ) {
                // Title and Favorite Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(b.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    IconButton(onClick = { viewModel.toggleFavorite() }) {
                        Icon(
                            imageVector = if (b.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Address Info
                Text("Address", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(b.address, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(b.category, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.height(32.dp))

                // Action Buttons
                ActionButtons(b)
                Spacer(modifier = Modifier.height(24.dp))

                // Image Gallery - ONLY shows if imageUrls is not empty
                if (b.imageUrls.isNotEmpty()) {
                    ImageGallery(imageUrls = b.imageUrls)
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Description - ONLY shows if description is not blank
                if (b.description.isNotBlank()) {
                    DescriptionSection(description = b.description)
                }

                Spacer(modifier = Modifier.height(24.dp)) //  padding at the bottom
            }
        }
    }
}

@Composable
private fun ImageGallery(imageUrls: List<String>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(imageUrls) { imageUrl ->
            // Coil's AsyncImage can tell us if it's loading
            var isLoading by remember { mutableStateOf(true) }

            Box( // This outer Box is for clipping the corners
                modifier = Modifier
                    .height(200.dp)
                    .aspectRatio(16 / 9f)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                AsyncImage( // The actual image, which is always present
                    model = imageUrl,
                    contentDescription = "Business Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    onState = { state ->
                        // The shimmer is turned off when the image is loaded or fails
                        isLoading = state is AsyncImagePainter.State.Loading
                    }
                )
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .shimmer()
                            .background(MidGray.copy(alpha = 0.5f))
                    )
                }
            }
        }
    }
}

@Composable
private fun DescriptionSection(description: String) {
    Column {
        Text(
            text = "Description",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


@Composable
private fun ActionButtons(business: Business) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Button(onClick = {
            val intent = Intent(Intent.ACTION_DIAL, "tel:${business.phone}".toUri())
            context.startActivity(intent)
        }) { Text("Call") }

        Button(
            onClick = { openWhatsApp(context, business.phone) },
            colors = ButtonDefaults.buttonColors(containerColor = Green)
        ) { Text("WhatsApp") }

        Button(onClick = {
            val intent = Intent(Intent.ACTION_VIEW, business.mapLink.toUri())
            context.startActivity(intent)
        }) { Text("Directions") }
    }
}

private fun openWhatsApp(context: Context, phone: String) {
    val completePhone = if (phone.startsWith("+")) phone else "+91$phone"
    val intent = Intent(Intent.ACTION_VIEW, "https://api.whatsapp.com/send?phone=$completePhone".toUri())
    intent.setPackage("com.whatsapp")
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "WhatsApp is not installed.", Toast.LENGTH_SHORT).show()
    }
}