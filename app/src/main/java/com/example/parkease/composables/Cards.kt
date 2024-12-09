package com.example.parkease.composables

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkease.utilities.ParkingLotData
import com.example.parkease.ui.theme.AppTheme
import com.example.parkease.utilities.BookingData

@Composable
fun ParkingSlotCard(
    isOccupied : Boolean,
    locationId: String,
    parkingSpaceId: String,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .size(100.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = {
            if (isOccupied.not()) {
                navController.navigate("bookingPage/${locationId}/${parkingSpaceId}")
            }
            else {
                Toast.makeText(
                    navController.context,
                    "$parkingSpaceId is occupied. Please select another space.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isOccupied) Color.Red else Color.Green), // Red if occupied, green otherwise
        ) {
            Text(
                text = parkingSpaceId.uppercase(),
                modifier = Modifier.align(Alignment.Center),
                style = AppTheme.typography.labelLargeSemiBold,
            )
        }
    }
}

@Composable
fun ParkingGrid(parkingSlots: List<ParkingLotData>, navController: NavController, locationId: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        parkingSlots.chunked(2).forEach { rowSlots ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowSlots.forEach { data: ParkingLotData ->
                    ParkingSlotCard(isOccupied = data.status == 1, locationId = locationId, parkingSpaceId = data.id, navController = navController)
                }
                if (rowSlots.size < 2) {
                    Spacer(modifier = Modifier.size(100.dp))
                }
            }
        }
    }
}