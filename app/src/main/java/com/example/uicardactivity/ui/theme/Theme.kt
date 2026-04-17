package com.example.uicardactivity.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AppDarkColorScheme = darkColorScheme()

@Composable
fun UICardActivityTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = AppDarkColorScheme,
        typography = AppTypography,
        content = content,
    )
}
