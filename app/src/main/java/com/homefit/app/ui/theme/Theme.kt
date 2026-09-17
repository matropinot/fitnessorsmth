package com.homefit.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes

private val LightColors = lightColorScheme(
    primary = Color(0xFF006C4A),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF8AF8C4),
    onPrimaryContainer = Color(0xFF002114),
    secondary = Color(0xFF4E6357),
    secondaryContainer = Color(0xFFD1E8D9),
    tertiary = Color(0xFF3F6374),
    tertiaryContainer = Color(0xFFC2E8FC),
    background = Color(0xFFFBFDF9),
    surface = Color(0xFFFBFDF9),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF6EDBA8),
    onPrimary = Color(0xFF003824),
    primaryContainer = Color(0xFF005235),
    onPrimaryContainer = Color(0xFF8AF8C4),
    secondary = Color(0xFFB5CCBE),
    secondaryContainer = Color(0xFF374A40),
    tertiary = Color(0xFFA6CCDF),
    tertiaryContainer = Color(0xFF264B5A),
    background = Color(0xFF101411),
    surface = Color(0xFF101411),
)

@Composable
fun HomeFitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colors = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && darkTheme -> dynamicDarkColorScheme(context)
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> dynamicLightColorScheme(context)
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
        shapes = Shapes(
            extraSmall = RoundedCornerShape(8.dp),
            small = RoundedCornerShape(12.dp),
            medium = RoundedCornerShape(18.dp),
            large = RoundedCornerShape(24.dp),
            extraLarge = RoundedCornerShape(32.dp),
        ),
        content = content,
    )
}
