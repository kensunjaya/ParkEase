package com.example.parkease

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
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
fun HomeScreen(
    name: String,
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    var locationData by remember { mutableStateOf<List<Location>?>(null) }
    var currentPage by remember { mutableStateOf(0) }
    var userData by remember { mutableStateOf<User?>(null) }

    // Fetch user data
    LaunchedEffect(Unit) {
        fetchDocument(
            collectionName = "users",
            documentId = Firebase.auth.currentUser!!.email!!,
            type = User::class.java,
            onSuccess = { data -> userData = data },
            onFailure = { exception -> println("Error: ${exception.message}") }
        )
    }

    // Fetch location data
    LaunchedEffect(Unit) {
        fetchCollection(
            collectionName = "locations",
            type = Location::class.java,
            onSuccess = { data -> locationData = data.filterNotNull() },
            onFailure = { exception -> println("Error: ${exception.message}") }
        )
    }

    if (locationData == null || userData == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Please wait...",
                style = AppTheme.typography.labelNormal,
                textAlign = TextAlign.Center
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
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Hi, ${userData!!.name}!",
            style = AppTheme.typography.labelLargeSemiBold,
            color = AppTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Welcome back to ParkEase. Let's find the perfect spot for your vehicle!",
            style = AppTheme.typography.labelNormal,
            textAlign = TextAlign.Center,
            color = AppTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))
        Carousel(locationData = locationData!!) { newPageIndex -> currentPage = newPageIndex }

        Spacer(modifier = Modifier.height(16.dp))

        when (userData?.activeParking) {
            null -> PrimaryButton(
                onClick = {
                    navController.navigate("viewParkingLot/${locationData!![currentPage].id}")
                },
                label = "Explore Available Spaces",
                disabled = locationData == null || userData == null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(width = 250.dp, height = 55.dp)
                    .align(Alignment.CenterHorizontally),
                color = AppTheme.colorScheme.bluePale,
                textColor = AppTheme.colorScheme.secondary
            )
            else -> Text(
                text = "You currently have an active parking session. End it before booking a new spot.",
                style = AppTheme.typography.labelNormal,
                textAlign = TextAlign.Center,
                color = AppTheme.colorScheme.anchor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}
