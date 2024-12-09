package com.example.parkease

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.parkease.composables.PrimaryButton
import com.example.parkease.composables.SecondaryButton
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

    if (userData == null) {
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

        Column(modifier = Modifier.padding(start = 40.dp)) {
            Text(
                text = "Name: ${userData!!.name}",
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Email: ${Firebase.auth.currentUser!!.email}",
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Active Parking: ${userData!!.activeParking}",
                 fontSize = 20.sp,
            )
        }

        Spacer(modifier = Modifier.height(100.dp))

        PrimaryButton(
            onClick =
            {
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