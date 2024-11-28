package com.example.parkease

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.parkease.composables.CustomTextField
import com.example.parkease.composables.PrimaryButton
import com.example.parkease.ui.theme.AppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@Composable

fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {

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
            text = "ParkEase",
            modifier = Modifier.fillMaxWidth(),
            color = AppTheme.colorScheme.primary,
            style = AppTheme.typography.titleLarge,
        )
        Text(
            text = "Parking Made Easier",
            modifier = Modifier.fillMaxWidth(),
            color = AppTheme.colorScheme.primary,
            style = AppTheme.typography.titleBig,
        )
        Spacer(modifier = Modifier.height(100.dp))

        Text(
            text = "Create an Account",
            style = AppTheme.typography.titleNormal,
            color = AppTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(
            placeholder = "Enter your full name",
            value = username,
            modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(),
            onValueChange = { newValue ->
                username = newValue
            }
        )
        CustomTextField(
            placeholder = "Enter your email",
            value = email,
            inputType = KeyboardType.Email,
            modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(),
            onValueChange = { newValue ->
                email = newValue
            }
        )

        CustomTextField(
            placeholder = "Enter your password",
            value = password,
            modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(),
            inputType = KeyboardType.Password,
            onValueChange = { newValue ->
                password = newValue
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
                    color = AppTheme.colorScheme.anchor,
                    modifier = Modifier.clickable { navController.navigate("login") }
                )
            }
            PrimaryButton (
                onClick = {
                    authViewModel.createAccount(
                        email = email,
                        password = password,
                        onSuccess = { user ->
                            Toast.makeText(
                                navController.context,
                                "Account created: ${user?.email}",
                                Toast.LENGTH_SHORT
                            ).show()
                            Firebase.auth.currentUser?.sendEmailVerification()
                            navController.navigate("verification/${Firebase.auth.currentUser?.email}") // Navigate on success
                        },
                        onFailure = { exception ->
                            Toast.makeText(
                                navController.context,
                                "Failed: ${exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                },
                label = "Register",
            )
        }
        Spacer(modifier = Modifier.height(150.dp))
    }
}