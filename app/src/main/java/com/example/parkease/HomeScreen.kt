package com.example.parkease

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.example.parkease.composables.ParkingGrid
import com.example.parkease.interfaces.ParkingLotData
import com.example.parkease.interfaces.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(name: String, navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    var result by remember { mutableStateOf<List<ParkingLotData>?>(null) }
    var userData by remember { mutableStateOf<User?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val db = FirebaseFirestore.getInstance()

    fun fetchDocument(
        collectionName: String,
        documentId: String,
        onSuccess: (User?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(collectionName).document(documentId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    onSuccess(user)
                } else {
                    onFailure(Exception("Document does not exist"))
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }


    LaunchedEffect(Unit) {
        result = fetchValuesWithOkHttp("http://192.168.90.63/getValues");
        fetchDocument(
            collectionName = "users",
            documentId = Firebase.auth.currentUser!!.email!!,
            onSuccess = { data -> userData = data

            },
            onFailure = { error ->
                println("Error fetching collection: $error")
            }
        )
        println(result);
    }



    suspend fun refetch() {
        result = fetchValuesWithOkHttp("http://192.168.90.63/getValues");
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Text("Welcome to Details Screen, ${Firebase.auth.currentUser?.displayName}", style = AppTheme.typography.labelLarge)
        when {
            userData == null -> Text(text = "Fetching user...")
            else -> Text(text = "Welcome back, ${userData!!.name}", style = AppTheme.typography.labelLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            result == null -> Text(text = "Loading...")
            result == emptyList<ParkingLotData>() ->
                Text(text = "Unavailable")

//            else -> Column {
//                result!!.forEach { item ->
//                    Text(text = "${item.id.uppercase()} : ${if (item.status == 1) "Occupied" else "Empty"}", style = AppTheme.typography.labelLarge)
//                }
//            }
            else -> {
                ParkingGrid(result!!)
            }
        }



        Spacer(modifier = Modifier.height(16.dp))

        ElevatedButton(
            onClick = {
                coroutineScope.launch {
                    refetch()
                }
            })
        {
            Text("Refresh")
        }

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