package com.example.parkease

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.parkease.composables.SecondaryButton

@Composable
fun SettingsPage(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Settings Page")
        Spacer(modifier = Modifier.height(16.dp))
        SecondaryButton(
            onClick =
            {
                authViewModel.signOut()
                navController.navigate("login")
            },
            label = "Sign Out"
        )
    }
}