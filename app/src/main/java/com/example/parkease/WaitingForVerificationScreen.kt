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
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MarkEmailUnread
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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
    var cooldownTimeLeft by remember { mutableIntStateOf(0) }
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
            .padding(horizontal = 24.dp, vertical = 48.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        androidx.compose.material3.Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.MarkEmailUnread,
                    contentDescription = "Email Verification",
                    modifier = Modifier.size(80.dp),
                    tint = AppTheme.colorScheme.primary
                )

                Text(
                    text = "We're almost there!",
                    style = AppTheme.typography.labelLargeSemiBold,
                    color = AppTheme.colorScheme.primary
                )
                Text(
                    text = "A verification email has been sent to:",
                    style = AppTheme.typography.labelNormal,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = email,
                    style = AppTheme.typography.labelNormalSemiBold,
                    color = AppTheme.colorScheme.anchor,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Please check your inbox and click the verification link",
                    style = AppTheme.typography.labelNormal,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        androidx.compose.material3.CircularProgressIndicator(
            modifier = Modifier.size(40.dp),
            color = AppTheme.colorScheme.bluePale
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "We're checking your email verification status...",
            style = AppTheme.typography.labelNormal,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SecondaryButton(
                onClick = {
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
                label = if (isCooldownActive) "Wait $cooldownTimeLeft seconds" else "Resend Email",
                disabled = isCooldownActive,
                color = AppTheme.colorScheme.bluePale,
            )
        }
    }
}
