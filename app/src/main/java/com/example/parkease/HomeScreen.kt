package com.example.parkease

import android.widget.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkease.ui.theme.AppTheme

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Welcome to Home Screen!",
            style = MaterialTheme.typography.labelLarge,
            color = AppTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = "",
            onValueChange = { /*TODO*/ },
            label = { Text("Enter your name") },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colorScheme.primary,
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                trailingIconColor = Color.Black,
                cursorColor = Color.Black,
                placeholderColor = Color.Black,
                focusedLabelColor = Color.Black,
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("details/Android") },
            elevation = ButtonDefaults.elevation(),
            border = ButtonDefaults.outlinedBorder,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text("Go to Details Screen", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}
