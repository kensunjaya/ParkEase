package com.example.parkease.composables

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.parkease.ui.theme.AppTheme

@Composable
fun CustomTextField(
    placeholder: String,
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        modifier = modifier,
        onValueChange = onValueChange,
        label = { Text(placeholder) },
        singleLine = true,
        textStyle = AppTheme.typography.body,
        colors = TextFieldDefaults.colors(
            focusedTextColor = AppTheme.colorScheme.primary,
            unfocusedTextColor = AppTheme.colorScheme.primary,
            disabledTextColor = AppTheme.colorScheme.onBackground.copy(alpha = 0.38f),
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            cursorColor = Color.Gray,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.Gray,
            focusedIndicatorColor = Color.Black,
        )
    )
}
