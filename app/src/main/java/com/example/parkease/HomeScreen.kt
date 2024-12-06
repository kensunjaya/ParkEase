package com.example.parkease

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
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
import com.example.parkease.composables.ParkingGrid
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
    var userData by remember { mutableStateOf<User?>(null) }
    val coroutineScope = rememberCoroutineScope()
//    val ip = "http://147.185.221.17:44176/getValues"

    // Get current user data, will also be used in ActiveParking page for checking user's active parking data
    LaunchedEffect(Unit) {
//        result = fetchValuesWithOkHttp(ip);
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
        println(result);
    }

    // Code snippets for fetching Parking Space data (occupied or not)
    // Later to be moved to a page after user choose Location
    LaunchedEffect(locationData) {
        if (locationData != null && locationData!!.isEmpty().not()) {
            result = fetchValuesWithOkHttp(locationData!![0].ip)
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
            result = fetchValuesWithOkHttp(locationData!![0].ip)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Text("Welcome to Details Screen, ${Firebase.auth.currentUser?.displayName}", style = AppTheme.typography.labelLarge)
        when {
            userData == null -> Text(text = "Fetching user...")
            else -> Text(text = "Welcome back, ${userData!!.name}", style = AppTheme.typography.labelLarge)
        }

        // Mapping locationData
        when {
            locationData == null -> Text(text = "Fetching locations...")
            else -> locationData!!.map { data ->
                Column {
                    Text(text = "Name: ${data.name}", style = AppTheme.typography.labelNormal)
                    Text(text = "Desc: ${data.description}", style = AppTheme.typography.labelNormal)
                    Text(text = "ip: ${data.ip}", style = AppTheme.typography.labelNormal)
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mapping parking lot data
        // Later to be moved to a Page after user choose Location
        when {
            result == null -> Text(text = "Loading...")
            result == emptyList<ParkingLotData>() ->
                Text(text = "Unavailable")
            else -> {
                ParkingGrid(result!!)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        // Later to be moved to a Page after user choose Location
        ElevatedButton(
            onClick = {
                coroutineScope.launch {
                    refetch()
                }
            })
        {
            Text("Refresh")
        }
    }
}