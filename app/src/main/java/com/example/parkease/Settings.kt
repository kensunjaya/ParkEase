package com.example.parkease

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.parkease.composables.CircularIndicator
import com.example.parkease.composables.PrimaryButton
import com.example.parkease.ui.theme.AppTheme
import com.example.parkease.utilities.User
import com.example.parkease.utilities.fetchDocument
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun SettingsPage(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    var userData by remember { mutableStateOf<User?>(null) }

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

    if (userData == null || Firebase.auth.currentUser == null) {
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

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "Settings Page",
            modifier = Modifier
                .padding(start = 16.dp)
                .padding(top = 50.dp),
            fontSize = 24.sp,
        )

        Spacer(modifier = Modifier.height(40.dp))

        androidx.compose.material3.Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, // Padding kiri
                    end = 16.dp,   // Padding kanan
                    top = 0.dp,   // Padding atas lebih besar
                    bottom = 32.dp)
                .height(150.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            )
        {
            Column(modifier = Modifier.padding(start = 20.dp, top = 22.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Filled.Person, // Replace with actual icon
                        contentDescription = "Profile",
                        tint = AppTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = userData!!.name,
                        style = AppTheme.typography.labelLargeSemiBold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Filled.Email, // Replace with actual icon
                        contentDescription = "Profile",
                        tint = AppTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "${Firebase.auth.currentUser!!.email}",
                        style = AppTheme.typography.labelNormalSemiBold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Filled.LocalParking, // Replace with actual icon
                        contentDescription = "Status",
                        tint = AppTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Active Parking: ${userData!!.activeParking}",
                        style = AppTheme.typography.labelNormalSemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        PrimaryButton(
            onClick = {
                authViewModel.signOut()
                navController.navigate("login")
            },
            label = "Sign Out",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(width = 200.dp, height = 60.dp),
            color = AppTheme.colorScheme.bluePale,
            textColor = AppTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(40.dp))

        PrimaryButton(
            onClick =
            {
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:") // Hanya aplikasi email yang merespons
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("ParkEase@gmail.com")) // Email tujuan
                    putExtra(Intent.EXTRA_SUBJECT, "Help Request") // Subjek email
                }
                navController.context.startActivity(emailIntent)
            },
            label = "Help",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(width = 200.dp, height = 60.dp),
            color = AppTheme.colorScheme.bluePale,
            textColor = AppTheme.colorScheme.secondary
        )
    }
}