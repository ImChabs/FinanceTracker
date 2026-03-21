package com.example.newfinancetracker.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Evergreen,
    onPrimary = Color.White,
    primaryContainer = Mint,
    onPrimaryContainer = DeepEvergreen,
    secondary = Seafoam,
    onSecondary = Color.White,
    secondaryContainer = SeafoamContainer,
    onSecondaryContainer = Slate,
    tertiary = Bronze,
    onTertiary = Color.White,
    tertiaryContainer = SandTint,
    onTertiaryContainer = Espresso,
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorContainerLight,
    onErrorContainer = ErrorRed,
    background = BackgroundLight,
    onBackground = Slate,
    surface = SurfaceLight,
    onSurface = Slate,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = Stone,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
    inverseSurface = Slate,
    inverseOnSurface = SurfaceLight,
    inversePrimary = EvergreenDark,
    surfaceTint = Evergreen
)

private val DarkColorScheme = darkColorScheme(
    primary = EvergreenDark,
    onPrimary = DeepEvergreen,
    primaryContainer = MintDark,
    onPrimaryContainer = Mint,
    secondary = SeafoamDark,
    onSecondary = DeepEvergreen,
    secondaryContainer = SeafoamContainerDark,
    onSecondaryContainer = SeafoamContainer,
    tertiary = BronzeDark,
    onTertiary = Espresso,
    tertiaryContainer = SandTintDark,
    onTertiaryContainer = SandTint,
    error = ErrorDark,
    onError = ErrorContainerDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = ErrorDark,
    background = BackgroundDark,
    onBackground = MistDark,
    surface = SurfaceDark,
    onSurface = MistDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = CloudDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    inverseSurface = SurfaceLight,
    inverseOnSurface = Slate,
    inversePrimary = Evergreen,
    surfaceTint = EvergreenDark
)

@Composable
fun FinanceTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = FinanceTrackerTypography,
        shapes = FinanceTrackerShapes,
        content = content
    )
}
