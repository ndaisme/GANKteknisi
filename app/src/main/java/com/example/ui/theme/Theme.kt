package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GankColorScheme = darkColorScheme(
    primary = Silver,
    onPrimary = BlackPrimary,
    primaryContainer = BlackSurfaceVariant,
    onPrimaryContainer = SilverLight,
    secondary = White,
    onSecondary = BlackPrimary,
    secondaryContainer = SilverGlass,
    onSecondaryContainer = White,
    tertiary = SilverLight,
    onTertiary = BlackPrimary,
    background = BlackPrimary,
    onBackground = White,
    surface = BlackSurface,
    onSurface = White,
    surfaceVariant = BlackSurfaceVariant,
    onSurfaceVariant = Silver,
    error = ErrorRed,
    onError = BlackPrimary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force dark theme for branding
    dynamicColor: Boolean = false, // Disable dynamic colors to strictly follow branding
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GankColorScheme,
        typography = Typography,
        content = content
    )
}
