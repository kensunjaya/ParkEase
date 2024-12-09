package com.example.parkease

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalParking
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.parkease.composables.TabView
import com.example.parkease.ui.theme.AppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import androidx.compose.runtime.getValue

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null
)

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val homeTab = TabBarItem(title = "Home", selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home)
            val activeParkingTab = TabBarItem(title = "Active Parking", selectedIcon = Icons.Filled.LocalParking, unselectedIcon = Icons.Outlined.LocalParking, badgeAmount = 7)
            val settingsTab = TabBarItem(title = "Settings", selectedIcon = Icons.Filled.Settings, unselectedIcon = Icons.Outlined.Settings)
            val tabBarItems = listOf(homeTab, activeParkingTab, settingsTab)

            AppTheme {
                val navController = rememberNavController()
                val user = Firebase.auth.currentUser
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val startDestination =  if (user != null && user.isEmailVerified) {
                    "Home"
                } else if (user != null && user.isEmailVerified.not()) {
                    "verification/${user.email}"
                } else {
                    "login"
                }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val validRoutes = tabBarItems.map { it.title } + listOf("viewParkingLot", "bookingPage")
                        if (currentRoute in validRoutes || currentRoute?.startsWith("viewParkingLot") == true || currentRoute?.startsWith("bookingPage") == true) {
                            TabView(tabBarItems, navController)
                        }
                    },
                    topBar = {
                        val validRoutes = tabBarItems.map { it.title } + listOf("viewParkingLot", "bookingPage")
                        if (currentRoute in validRoutes || currentRoute?.startsWith("viewParkingLot") == true || currentRoute?.startsWith("bookingPage") == true) {
                            TopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = AppTheme.colorScheme.softBlue,
                                    titleContentColor = AppTheme.colorScheme.primary,
                                ),
                                title = {
                                    Text("ParkEase", style = AppTheme.typography.labelLargeSemiBold)
                                }
                            )
                        }
                    },
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier
                            .padding(innerPadding)
                            .background(AppTheme.colorScheme.background)
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

                        composable("viewParkingLot/{locationId}") { navBackStackEntry ->
                            val locationId = navBackStackEntry.arguments?.getString("locationId")
                            if (locationId != null) {
                                ViewParkingLot(locationId = locationId, navController = navController)
                            } else {
                                // Handle case where locationId is null
                                Log.e("NavigationError", "Location ID argument is missing")
                            }
                        }
                        composable(
                            route = "bookingPage/{locationId}/{parkingSpaceId}"
                        ) { navBackStackEntry ->
                            val locationId = navBackStackEntry.arguments?.getString("locationId")
                            val parkingSpaceId = navBackStackEntry.arguments?.getString("parkingSpaceId")

                            if (locationId != null && parkingSpaceId != null) {
                                BookingPage(
                                    locId = locationId,
                                    parkingSpaceId = parkingSpaceId,
                                    navController = navController
                                )
                            } else {
                                Log.e("NavigationError", "Required arguments are missing")
                            }
                        }


//                        composable("home/{name}") { backStackEntry ->
//                            val name = backStackEntry.arguments?.getString("name")
//                            HomeScreen(name = name ?: "Unknown", navController)
//                        }
                        composable(homeTab.title) {
                            HomeScreen(name = user?.displayName ?: "Unknown", navController)
                        }
                        composable(activeParkingTab.title) {
                            ActiveParkingScreen(navController)
                        }
                        composable(settingsTab.title) {
                            SettingsPage(navController)
                        }
                    }
                }
            }
        }
    }
}