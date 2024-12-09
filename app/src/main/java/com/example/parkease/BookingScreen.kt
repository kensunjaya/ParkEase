package com.example.parkease

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.parkease.composables.SecondaryButton
import com.example.parkease.ui.theme.AppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberAsyncImagePainter
import com.example.parkease.composables.Carousel
import com.example.parkease.composables.ParkingGrid
import com.example.parkease.utilities.Location
import com.example.parkease.utilities.ParkingLotData
import com.example.parkease.utilities.User
import com.example.parkease.utilities.fetchCollection
import com.example.parkease.utilities.fetchDocument
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


@Composable
fun BookingPage(locationId: String, parkingSpaceId: String, navController: NavController) {
    var locationData by remember { mutableStateOf<Location?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Fetch location data
    LaunchedEffect(Unit) {
        fetchDocument(
            collectionName = "locations",
            documentId = locationId,
            type = Location::class.java,
            onSuccess = { data ->
                locationData = data
                println("Fetched location: $locationData")
            },
            onFailure = { exception ->
                println("Error: ${exception.message}")
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Display loading or fetched data
        when {
            locationData == null -> {
                Text(
                    text = "Loading location details...",
                    style = AppTheme.typography.titleBig,
                    color = AppTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            else -> {
                // Location details
                Image(
                    painter = rememberAsyncImagePainter(model = locationData!!.thumbnail),
                    contentDescription = locationData!!.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = locationData!!.name,
                    style = AppTheme.typography.titleBig,
                    color = AppTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Parking Slot: $parkingSpaceId",
                    style = AppTheme.typography.labelLargeSemiBold,
                    color = AppTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Location Details:",
                        style = AppTheme.typography.labelLargeSemiBold,
                        color = AppTheme.colorScheme.primary
                    )
                    Text(
                        text = locationData!!.description,
                        style = AppTheme.typography.labelNormal,
                        color = AppTheme.colorScheme.primary
                    )
                    Text(
                        text = "Cost per Hour: Rp ${locationData!!.costPerHour}",
                        style = AppTheme.typography.labelNormalSemiBold,
                        color = AppTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                ElevatedButton(
                    onClick = {
                        coroutineScope.launch {
                            // Nanti diisi action buat logic booking parking space
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Confirm Booking",
                        style = AppTheme.typography.labelLargeSemiBold
                    )
                }
            }
        }
    }
}
