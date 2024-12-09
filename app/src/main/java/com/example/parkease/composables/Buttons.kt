package com.example.parkease.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.parkease.ui.theme.AppTheme

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    label: String,
    disabled: Boolean = false,
    color: Color = AppTheme.colorScheme.primary,
    textColor: Color = Color.White,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = !disabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            disabledContainerColor = Color.Gray,
            contentColor = AppTheme.colorScheme.onPrimary,
        ),
        shape = AppTheme.shape.button,
    ) {
        Text(
            text = label,
            style = AppTheme.typography.labelNormal,
            color = textColor,
        )
    }
}

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    label: String,
    disabled: Boolean = false,
    color: Color = Color.Transparent,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = !disabled,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Gray,
            contentColor = AppTheme.colorScheme.primary,
        ),
        shape = AppTheme.shape.button,
        border = BorderStroke(2.dp, AppTheme.colorScheme.primary),
    ) {
        Text(
            text = label,
            style = AppTheme.typography.labelNormal,
            color = AppTheme.colorScheme.primary,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonsPreviewTogether() {
    AppTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            PrimaryButton(
                label = "Primary Button",
                onClick = { /* No action for preview */ }
            )
            SecondaryButton(
                label = "Secondary Button",
                onClick = { /* No action for preview */ }
            )
        }
    }
}

