package com.example.parkease

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkease.ui.theme.AppTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import com.example.parkease.composables.CircularIndicator
import com.example.parkease.composables.ParkingGrid
import com.example.parkease.composables.PrimaryButton
import com.example.parkease.utilities.Location
import com.example.parkease.utilities.ParkingLotData
import com.example.parkease.utilities.fetchDocument
import com.example.parkease.utilities.fetchValuesWithOkHttp
import kotlinx.coroutines.launch
import kotlin.random.Random


@Composable
fun ViewParkingLot(locationId: String, navController: NavController) {
    var result by remember { mutableStateOf<List<ParkingLotData>?>(null) }
    var locationData by remember{ mutableStateOf<Location?>(null) }
    var isRandomData by remember{ mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Code snippets for fetching Parking Space data (occupied or not)
    // Later to be moved to a page after user choose Location
    LaunchedEffect(locationData) {
        if (locationData != null) {
            result = fetchValuesWithOkHttp(locationData!!.ip)
        }
    }

    // Call firebase API for fetching locationData
    LaunchedEffect(Unit) {
        fetchDocument(
            collectionName = "locations",
            documentId = locationId,
            type = Location::class.java,
            onSuccess = { data ->
                locationData = data
                println("Fetched locations: $locationData")
            },
            onFailure = { exception ->
                println("Error: ${exception.message}")
            }
        )
    }

    // Refresh parking space data (refetch data)
    // Later to be moved to a Page after user choose Location
    suspend fun refetch() {
        if (locationData != null) {
            result = null
            result = fetchValuesWithOkHttp(locationData!!.ip)
        }
        if (result == emptyList<ParkingLotData>() && locationData != null) {
            val rand = Random(System.currentTimeMillis())
            result = List(rand.nextInt(4, 98)) {
                ParkingLotData(
                    id = "P${it+1}",
                    status = rand.nextInt(0, 2),
                )
            }
            isRandomData = true
            Toast.makeText(
                navController.context,
                "Connection Timeout. Server is possibly down or unreachable. Showing random dummy data instead.",
                Toast.LENGTH_SHORT
            ).show()

        }
        else {
            isRandomData = false
        }
    }

    if (locationData == null || result == null)  {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Fetching Data",
                style = AppTheme.typography.labelNormal
            )
            Spacer(modifier = Modifier.height(32.dp))
            CircularIndicator()
            Spacer(modifier = Modifier.height(32.dp))
        }
        return
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Mapping parking lot data
        // Later to be moved to a Page after user choose Location

        when {
            locationData == null -> Text(text = "Loading...")
            else -> {
                Text(
                    text = locationData!!.name,
                    style = AppTheme.typography.titleBig,
                    color = AppTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (result) {
            null -> Text(text = "Loading...")
            emptyList<ParkingLotData>() ->
                Text(text = "Connection Timeout. Server is possibly down or unreachable.")
            else -> {
                if (isRandomData) {
                    Text(
                        text = "Showing random dummy data",
                        style = AppTheme.typography.labelNormal,
                        color = AppTheme.colorScheme.anchor,
                    )
                }
                ParkingGrid(
                    parkingSlots = result!!,
                    navController = navController,
                    locationId = locationId
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton(
            onClick = {
                coroutineScope.launch {
                    refetch()
                }
            },
            label = "Refresh",
            disabled = result == null,
            modifier = Modifier
                .padding(8.dp)
                .size(width = 200.dp, height = 50.dp)
                .align(Alignment.CenterHorizontally),
            color = AppTheme.colorScheme.bluePale,
        )
    }
}