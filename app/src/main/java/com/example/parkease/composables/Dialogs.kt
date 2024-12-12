package com.example.parkease.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.parkease.ui.theme.AppTheme

@Composable
fun ConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    onlyShowConfirmButton: Boolean = false,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Alert icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = AppTheme.colorScheme.bluePale
                )
            ) {
                Text("Confirm")
            }
        },

        dismissButton = if (!onlyShowConfirmButton) {{
            TextButton(
                onClick = {
                    onDismissRequest()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = AppTheme.colorScheme.bluePale
                )
            ) {
                Text("Dismiss")
            }
        }} else {{
            TextButton(
                onClick = {
                    onDismissRequest()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Text("")
            }
        }}
    )
}