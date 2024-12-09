package com.example.parkease.composables

import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.parkease.ui.theme.AppTheme

@Composable
fun CircularIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.width(64.dp),
        color = AppTheme.colorScheme.primary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}