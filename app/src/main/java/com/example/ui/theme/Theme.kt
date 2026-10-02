package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ForMaxColorScheme = darkColorScheme(
    primary = ForMaxElectricGreen,
    onPrimary = Color(0xFF0B0D0F),
    primaryContainer = Color(0xFF00391A),
    onPrimaryContainer = ForMaxElectricGreen,
    secondary = ForMaxGreenAccent,
    onSecondary = Color(0xFF0B0D0F),
    secondaryContainer = Color(0xFF1E3812),
    onSecondaryContainer = Color(0xFFB9F6CA),
    tertiary = ForMaxBlue,
    onTertiary = Color.White,
    background = ForMaxBackground,
    onBackground = ForMaxTextPrimary,
    surface = ForMaxSurface,
    onSurface = ForMaxTextPrimary,
    surfaceVariant = ForMaxSurfaceVariant,
    onSurfaceVariant = ForMaxTextSecondary,
    outline = ForMaxCardBorder,
    outlineVariant = ForMaxDivider,
    error = ForMaxError,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // FOR MAX is Dark-first UI
    dynamicColor: Boolean = false, // Keep branded high-contrast electric green palette
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ForMaxColorScheme,
        typography = Typography,
        content = content
    )
}
