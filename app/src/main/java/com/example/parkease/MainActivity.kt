package com.example.parkease

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.parkease.ui.theme.AppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()
                val user = Firebase.auth.currentUser
                val startDestination =  if (user != null && user.isEmailVerified) {
                    "home/${user.displayName}"
                } else if (user != null && user.isEmailVerified.not()) {
                    "verification/${user.email}"
                } else {
                    "login"
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(innerPadding).background(AppTheme.colorScheme.background)
                    ) {
                        composable("register") {
                            RegisterScreen(navController)
                        }
                        composable("login") {
                            LoginScreen(navController)
                        }
                        composable("verification/{email}") { navBackStackEntry ->
                            val email = navBackStackEntry.arguments?.getString("email")
                            if (email != null) {
                                WaitingForVerificationScreen(email = email, navController = navController)
                            } else {
                                // Handle case where email is null
                                Log.e("NavigationError", "Email argument is missing")
                            }
                        }
                        composable("home/{name}") { backStackEntry ->
                            val name = backStackEntry.arguments?.getString("name")
                            HomeScreen(name = name ?: "Unknown", navController)
                        }
                    }
                }
            }
        }
    }
}