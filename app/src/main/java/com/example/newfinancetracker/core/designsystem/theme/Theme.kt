package com.example.newfinancetracker.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Evergreen,
    onPrimary = Mist,
    primaryContainer = Mint,
    onPrimaryContainer = Pine,
    secondary = Seafoam,
    background = Mist,
    onBackground = Slate,
    surface = Mist,
    onSurface = Slate,
    surfaceVariant = Cloud,
    onSurfaceVariant = Stone
)

private val DarkColorScheme = darkColorScheme(
    primary = Seafoam,
    onPrimary = Pine,
    primaryContainer = Pine,
    onPrimaryContainer = Mint,
    secondary = Evergreen,
    background = Night,
    onBackground = Mist,
    surface = Night,
    onSurface = Mist,
    surfaceVariant = ForestMist,
    onSurfaceVariant = Cloud
)

@Composable
fun FinanceTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = FinanceTrackerTypography,
        content = content
    )
}
