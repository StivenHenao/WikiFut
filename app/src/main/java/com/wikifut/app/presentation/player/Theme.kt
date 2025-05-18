package com.wikifut.app.presentation.player

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val DarkColorPalette = darkColorScheme(
    primary = Color(0xFF1F1235),
    onPrimary = Color.White,
    surface = Color(0xFF1E1E1E),
    // Agrega otros colores según necesites
)

private val LightColorPalette = lightColorScheme(
    primary = Color(0xFF6200EE),
    onPrimary = Color.Black,
    surface = Color.White
    // Configura otros colores para el tema claro
)

@Composable
fun Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colorScheme = colors,
        typography = MaterialTheme.typography.copy(
            bodyLarge = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            headlineSmall = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            titleMedium = TextStyle(
                fontSize = 14.sp,
                color = Color.LightGray
            ),
            labelLarge = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        ),
        content = content
    )
}

// Extensión para estilos personalizados (ej: rating)
val MaterialTheme.ratingText: TextStyle
    @Composable get() = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        shadow = Shadow(color = Color.Black)
    )