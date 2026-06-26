package com.example.paytrack.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = LightBluePrimary,
    onPrimary = Color.White,
    secondary = SecondaryBlue,

    // ✅ Light = أبيض/فاتح
    background = Color(0xFFF5F9FF),
    surface = Color.White,
    onBackground = Color(0xFF0D47A1),
    onSurface = Color.Black,

    error = ErrorRed
)

private val DarkColors = darkColorScheme(
    primary = LightBluePrimary,
    onPrimary = Color.White,
    secondary = SecondaryBlue,

    // ✅ Dark = أزرق غامق
    background = Color(0xFF0B1E2D),
    surface = Color(0xFF102C3A),
    onBackground = Color(0xFFD6E9FF),
    onSurface = Color.White,

    error = ErrorRed
)

@Composable
fun PayTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}