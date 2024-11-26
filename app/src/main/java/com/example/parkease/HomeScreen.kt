package com.example.parkease

import android.widget.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkease.composables.CustomTextField
import com.example.parkease.composables.PrimaryButton
import com.example.parkease.ui.theme.AppTheme

@Composable
fun HomeScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Welcome to ParkEase!",
            style = AppTheme.typography.titleNormal,
            color = AppTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(
            placeholder = "Enter your full name",
            value = username,
            modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(),
            onValueChange = { newValue ->
                username = newValue // Update the state when the user types
            }
        )
        CustomTextField(
            placeholder = "Enter your email",
            value = email,
            modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(),
            onValueChange = { newValue ->
                email = newValue // Update the state when the user types
            }
        )

        CustomTextField(
            placeholder = "Enter your password",
            value = password,
            modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(),
            onValueChange = { newValue ->
                password = newValue // Update the state when the user types
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Row {
                Text(
                    text = "Already have an account?",
                    style = AppTheme.typography.labelNormal,
                    color = AppTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.padding(start = 4.dp))
                Text(
                    text = "Login",
                    style = AppTheme.typography.labelNormalSemiBold,
                    color = AppTheme.colorScheme.anchor
                )
            }

            PrimaryButton (
                onClick = { navController.navigate("details/Android") },
                label = "Register",
            )
        }
    }
}