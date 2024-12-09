package com.example.parkease

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
import androidx.compose.ui.text.style.TextAlign
import com.example.parkease.composables.Carousel
import com.example.parkease.composables.CircularIndicator
import com.example.parkease.composables.ParkingGrid
import com.example.parkease.composables.PrimaryButton
import com.example.parkease.utilities.Location
import com.example.parkease.utilities.ParkingLotData
import com.example.parkease.utilities.User
import com.example.parkease.utilities.fetchCollection
import com.example.parkease.utilities.fetchDocument
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(name: String, navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    var result by remember { mutableStateOf<List<ParkingLotData>?>(null) }
    var locationData by remember{ mutableStateOf<List<Location>?>(null) }
    var currentPage by remember { mutableStateOf(0) }
    var userData by remember { mutableStateOf<User?>(null) }
    val coroutineScope = rememberCoroutineScope()
//    val ip = "http://147.185.221.17:44176/getValues"

    // Get current user data, will also be used in ActiveParking page for checking user's active parking data
    LaunchedEffect(Unit) {
        fetchDocument(
            collectionName = "users",
            documentId = Firebase.auth.currentUser!!.email!!,
            type = User::class.java,
            onSuccess = {
                    data -> userData = data
            },
            onFailure = { exception ->
                println("Error: ${exception.message}")
            }
        )
    }

    // Code snippets for fetching Parking Space data (occupied or not)
    // Later to be moved to a page after user choose Location
    LaunchedEffect(locationData) {
        if (locationData != null && locationData!!.isEmpty().not()) {
            result = fetchValuesWithOkHttp(locationData!![1].ip)
        }
    }

    // Call firebase API for fetching locationData
    LaunchedEffect(Unit) {
        fetchCollection(
            collectionName = "locations",
            type = Location::class.java,
            onSuccess = { data ->
                locationData = data.filterNotNull()
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
        if (locationData != null && locationData!!.isEmpty().not()) {
            result = fetchValuesWithOkHttp(locationData!![1].ip)
        }
    }

    if (locationData == null || result == null || userData == null)  {
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
        when {
            userData == null -> Text(text = "Fetching user...")
            else -> Text(text = "Welcome back, ${userData!!.name}", style = AppTheme.typography.labelLargeSemiBold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            userData?.activeParking != null -> {
                Text(text = "You have an active parking session", style = AppTheme.typography.labelNormalSemiBold, color = AppTheme.colorScheme.anchor)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            locationData == null -> Text(text = "Loading...")
            else -> Carousel(locationData = locationData!!) { newPageIndex ->
                currentPage = newPageIndex
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mapping parking lot data
        // Later to be moved to a Page after user choose Location
//        when {
//            result == null -> Text(text = "Loading...")
//            result == emptyList<ParkingLotData>() ->
//                Text(text = "Unavailable")
//            else -> {
//                ParkingGrid(result!!)
//            }
//        }

        // Later to be moved to a Page after user choose Location

        when (userData?.activeParking) {
            null -> ElevatedButton(
                onClick = {
                    navController.navigate("viewParkingLot/${locationData!![currentPage].id}")
                })
            {
                Text("View Parking Lot")
            }
            else -> Text(
                text = "You must end your current parking session before booking a new one.",
                style = AppTheme.typography.labelNormal,
                color = AppTheme.colorScheme.anchor,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                textAlign = TextAlign.Center
            )
        }

    }
}