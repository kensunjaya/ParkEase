package com.example.parkease

import android.graphics.drawable.Icon
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
import com.example.parkease.ui.theme.AppTheme
import com.example.parkease.utilities.ActiveParking
import com.example.parkease.utilities.User
import com.example.parkease.utilities.fetchDocument
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

@Composable
fun ActiveParkingScreen(navController: NavController) {
    var userData by remember { mutableStateOf<User?>(null) }
    var activeParkingData by remember { mutableStateOf<ActiveParking?>(null) }

    LaunchedEffect(Unit) {
        fetchDocument(
            collectionName = "users",
            documentId = Firebase.auth.currentUser!!.email!!,
            type = User::class.java,
            onSuccess = { data ->
                userData = data
                if (data?.activeParking != null) {
                    fetchDocument(
                        collectionName = "locations",
                        documentId = data.activeParking.id,
                        type = ActiveParking::class.java,
                        onSuccess = { parkingData ->
                            if (parkingData != null) {
                                userData?.activeParking?.costPerHour = parkingData.costPerHour
                                userData?.activeParking?.name = parkingData.name
                                userData?.activeParking?.description = parkingData.description
                                activeParkingData = userData?.activeParking
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Your Parking Session",
            style = AppTheme.typography.labelLargeSemiBold,
            color = AppTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (activeParkingData != null) {
            val startMillis = activeParkingData?.start!!.toDate().time
            val currentMillis = System.currentTimeMillis()
            val hoursElapsed = (currentMillis - startMillis) / 3600000.0
            val currentTotalCost = activeParkingData?.costPerHour?.times(ceil(hoursElapsed))
            val numberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID")) as DecimalFormat
            val symbols = numberFormat.decimalFormatSymbols
            symbols.currencySymbol = "Rp "
            numberFormat.decimalFormatSymbols = symbols
            numberFormat.maximumFractionDigits = 2

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
                            text = activeParkingData?.name ?: "N/A",
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
                            text = activeParkingData?.parkingSlotId ?: "N/A",
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
                            text = "Started at: ${activeParkingData?.start!!.toDate()}",
                            style = AppTheme.typography.labelNormal
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Filled.AttachMoney, // Replace with actual icon
                            contentDescription = "Cost",
                            tint = AppTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Cost per hour: ${numberFormat.format(activeParkingData?.costPerHour)}",
                            style = AppTheme.typography.labelNormal
                        )
                    }

                    Text(
                        text = "You have been parked for ${hoursElapsed.roundToInt()} hours.",
                        style = AppTheme.typography.labelNormal
                    )

                    Text(
                        text = "Total cost: ${numberFormat.format(currentTotalCost)}",
                        style = AppTheme.typography.labelNormalSemiBold,
                        color = AppTheme.colorScheme.anchor
                    )
                }
            }

//            ElevatedButton(
//                onClick = {
//                    navController.navigate("stopParking")
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp),
//                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
//            ) {
//                Text(text = "End Parking Session")
//            }
        } else {
            Text(
                text = "You don't have any active parking sessions.",
                style = AppTheme.typography.labelNormal,
                textAlign = TextAlign.Center
            )
        }
    }
}

