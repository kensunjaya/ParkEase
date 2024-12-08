package com.example.parkease.composables

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
import com.example.parkease.utilities.ParkingLotData
import com.example.parkease.ui.theme.AppTheme

@Composable
fun ParkingSlotCard(
    isOccupied : Boolean,
    label: String
) {
    Card(
        modifier = Modifier
            .size(100.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isOccupied) Color.Red else Color.Green), // Red if occupied, green otherwise
        ) {
            Text(
                text = label.uppercase(),
                modifier = Modifier.align(Alignment.Center),
                style = AppTheme.typography.labelLargeSemiBold,
            )
        }
    }
}

@Composable
fun ParkingGrid(parkingSlots: List<ParkingLotData>) {
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
                    ParkingSlotCard(isOccupied = data.status == 1, label = data.id)
                }
                if (rowSlots.size < 2) {
                    Spacer(modifier = Modifier.size(100.dp))
                }
            }
        }
    }
}