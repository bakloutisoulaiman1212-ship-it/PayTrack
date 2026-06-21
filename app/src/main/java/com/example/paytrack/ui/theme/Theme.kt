package com.example.paytrack.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ✅ Light Theme (اللي باش يستعملو التطبيق)
private val LightColors = lightColorScheme(

    primary = LightBluePrimary,
    onPrimary = Color.White,

    secondary = SecondaryBlue,

    background = Color(0xFF0B1F3A), // ✅ DARK BLUE
    surface = Color(0xFF0B1F3A),

    onBackground = Color.White,
    onSurface = Color.White,

    error = ErrorRed
)


// ✅ Dark Theme (اختياري 💡)
private val DarkColors = darkColorScheme(

    primary = LightBlueDark,
    onPrimary = Color.White,

    secondary = SecondaryDark,

    background = Color(0xFF0A1A2F),
    surface = Color(0xFF102840),

    onBackground = Color.White,
    onSurface = Color.White,

    error = ErrorRed
)


// ✅ Main Theme
@Composable
fun PayTrackTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {

    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}