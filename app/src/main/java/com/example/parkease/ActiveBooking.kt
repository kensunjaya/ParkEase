package com.example.parkease

import android.graphics.drawable.Icon
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkease.composables.CircularIndicator
import com.example.parkease.composables.ConfirmationDialog
import com.example.parkease.composables.PrimaryButton
import com.example.parkease.ui.theme.AppTheme
import com.example.parkease.utilities.ActiveParking
import com.example.parkease.utilities.Booking
import com.example.parkease.utilities.User
import com.example.parkease.utilities.editDocumentField
import com.example.parkease.utilities.fetchDocument
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun ActiveBookingScreen(navController: NavController) {
    var userData by remember { mutableStateOf<User?>(null) }
    var bookingData by remember { mutableStateOf<Booking?>(null) }
    val openAlertDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        fetchDocument(
            collectionName = "users",
            documentId = Firebase.auth.currentUser!!.email!!,
            type = User::class.java,
            onSuccess = { data ->
                userData = data
                if (data?.booking != null) {
                    fetchDocument(
                        collectionName = "locations",
                        documentId = data!!.booking!!.locationId,
                        type = Booking::class.java,
                        onSuccess = { parkingData ->
                            if (parkingData != null) {
                                userData?.booking?.costPerHour = parkingData.costPerHour
                                userData?.booking?.name = parkingData.name
                                userData?.booking?.description = parkingData.description
                                bookingData = userData?.booking
                            }
                        },
                        onFailure = { exception ->
                            println("Error: ${exception.message}")
                        }
                    )
                }
            },
            onFailure = { exception ->
                println("Error: ${exception.message}")
            }
        )
    }

    if (userData == null)  {
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
                        value = null,
                        onSuccess = { success ->
                            if (success) {
                                println("Parking session started")
                            }
                        },
                        onFailure = { exception ->
                            println("Error: ${exception.message}")
                        }
                    )
                    Toast.makeText(
                        navController.context,
                        "You've confirmed your booking. Your parking billing starts from now.",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                dialogTitle = "Confirmation",
                dialogText = "Are you in the parking lot? Confirm to start your parking session.",
                icon = Icons.Default.Info
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Your active booking session",
            style = AppTheme.typography.labelLargeSemiBold,
            color = AppTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (bookingData != null) {
            val startMillis = bookingData?.start!!.toDate().time
            val currentMillis = System.currentTimeMillis()
            val minutesElapsed = (currentMillis - startMillis) / 60000.0
            if (minutesElapsed >= 10) {
                editDocumentField(
                    collectionName = "users",
                    documentName = Firebase.auth.currentUser!!.email!!,
                    field = "booking",
                    value = null,
                    onSuccess = { success ->
                        if (success) {
                            println("Parking session cancelled")
                            userData!!.booking = null
                        }
                    },
                    onFailure = { exception ->
                        println("Error: ${exception.message}")
                    }
                )
                Toast.makeText(
                    navController.context,
                    "Your booking session has expired. Please book a new parking space.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            val hoursElapsed = (currentMillis - startMillis) / 3600000.0

            // Information Card
            androidx.compose.material3.Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Filled.LocationOn, // Replace with actual icon
                            contentDescription = "Location",
                            tint = AppTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = bookingData?.name ?: "N/A",
                            style = AppTheme.typography.labelLargeSemiBold
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Filled.MyLocation, // Replace with actual icon
                            contentDescription = "Location",
                            tint = AppTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = bookingData?.parkingSlotId ?: "N/A",
                            style = AppTheme.typography.labelLargeSemiBold
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Filled.WatchLater, // Replace with actual icon
                            contentDescription = "Start",
                            tint = AppTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp) // Adjust size as needed
                        )
                        Text(
                            text = "Started at: ${bookingData?.start!!.toDate()}",
                            style = AppTheme.typography.labelNormal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Your active booking will automatically cancelled in ${String.format("%.2f", 10 - minutesElapsed)} minutes.",
            )
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                onClick = {
                    openAlertDialog.value = true
                },
                label = "Confirm Booking",
                disabled = bookingData == null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(width = 250.dp, height = 50.dp)
                    .align(Alignment.CenterHorizontally),
                color = AppTheme.colorScheme.bluePale,
                textColor = AppTheme.colorScheme.secondary
            )

        } else {
            Text(
                text = "You haven't booked any parking space.",
                style = AppTheme.typography.labelNormal,
                textAlign = TextAlign.Center
            )
        }
    }
}

