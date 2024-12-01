package com.example.parkease

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.parkease.composables.PrimaryButton
import com.example.parkease.composables.SecondaryButton
import com.example.parkease.ui.theme.AppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay

@Composable
fun WaitingForVerificationScreen(
    email: String,
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    var isCooldownActive by remember { mutableStateOf(false) }
    var cooldownTimeLeft by remember { mutableStateOf(0) }
    var isVerified by remember { mutableStateOf(false) }

    LaunchedEffect(isCooldownActive) {
        if (isCooldownActive) {
            cooldownTimeLeft = 60
            while (cooldownTimeLeft > 0) {
                delay(1000L) // Wait for 1 second
                cooldownTimeLeft--
            }
            isCooldownActive = false
        }
    }

    LaunchedEffect(true) {
        while (!isVerified) {
            delay(3000L) // Check every 3 seconds
            Firebase.auth.currentUser?.reload()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isVerified = Firebase.auth.currentUser?.isEmailVerified ?: false
                    if (isVerified) {
                        navController.navigate("Home")
                    }
                } else {
                    Toast.makeText(
                        navController.context,
                        "Failed to fetch status",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "A verification email has been sent to $email",
            style = AppTheme.typography.labelLargeSemiBold,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.fillMaxWidth()) {

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            SecondaryButton(
                onClick =
                {
                    authViewModel.signOut()
                    navController.navigate("login")
                },
                label = "Go Back"
            )
            PrimaryButton(
                onClick = {
                    Firebase.auth.currentUser?.sendEmailVerification()
                    Toast.makeText(
                        navController.context,
                        "Verification email sent",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCooldownActive = true
                },
                label = if (isCooldownActive) "Wait $cooldownTimeLeft seconds" else "Resend email verification",
                disabled = isCooldownActive
            )
        }


    }
}