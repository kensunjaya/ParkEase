package com.example.parkease

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkease.ui.theme.AppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberAsyncImagePainter
import com.example.parkease.composables.CircularIndicator
import com.example.parkease.composables.ConfirmationDialog
import com.example.parkease.composables.PrimaryButton
import com.example.parkease.utilities.Booking
import com.example.parkease.utilities.Location
import com.example.parkease.utilities.editDocumentField
import com.example.parkease.utilities.fetchDocument
import com.google.firebase.Timestamp

@Composable
fun BookingPage(locId: String, parkingSpaceId: String, navController: NavController) {
    var locationData by remember { mutableStateOf<Location?>(null) }
    val openAlertDialog = remember { mutableStateOf(false) }

    // Fetch location data
    LaunchedEffect(Unit) {
        fetchDocument(
            collectionName = "locations",
            documentId = locId,
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

    if (locationData == null)  {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            androidx.compose.material.Text(
                text = "Fetching Data",
                style = AppTheme.typography.labelNormal
            )
            Spacer(modifier = Modifier.height(32.dp))
            CircularIndicator()
            Spacer(modifier = Modifier.height(32.dp))
        }
        return
    }

    when {
        openAlertDialog.value -> {
            ConfirmationDialog(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = {
                    openAlertDialog.value = false
                    editDocumentField(
                        collectionName = "users",
                        documentName = Firebase.auth.currentUser!!.email!!,
                        field = "booking",
                        value = Booking(
                            end = Timestamp((System.currentTimeMillis() + 600000) / 1000, 0)
                        ).apply {
                            parkingSlotId = parkingSpaceId
                            locationId = locId
                            start = Timestamp(System.currentTimeMillis() / 1000, 0)
                        },
                        onSuccess = { success ->
                            if (success) {
                                println("Successfully booked parking space $parkingSpaceId")
                                navController.navigate("Reservation")
                            }
                        },
                        onFailure = { exception ->
                            println("Error: ${exception.message}")
                        }
                    )
                    Toast.makeText(
                        navController.context,
                        "You've successfully booked $parkingSpaceId. You have 10 minutes remaining to park your vehicle.",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                dialogTitle = "Confirm Booking",
                dialogText = "Are you sure you want to book parking space $parkingSpaceId? Once confirmed, you have 10 minutes to park your vehicle.",
                icon = Icons.Default.Info
            )
        }
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

                PrimaryButton(
                    onClick = {
                        openAlertDialog.value = true
                    },
                    label = "Confirm Booking",
                    disabled = locationData == null,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(width = 250.dp, height = 50.dp)
                        .align(Alignment.CenterHorizontally),
                    color = AppTheme.colorScheme.bluePale,
                    textColor = AppTheme.colorScheme.secondary
                )
            }
        }
    }
}
