package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = PrimaryBlueText,
    primaryContainer = PrimaryBlueContainer,
    onPrimaryContainer = OnPrimaryBlueContainer,
    secondary = TunisiaRed,
    onSecondary = Color.White,
    tertiary = GoldAccent,
    onTertiary = Color.Black,
    background = ElegantDarkBackground,
    onBackground = TextPrimaryDark,
    surface = ElegantDarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = ElegantDarkSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    outline = ElegantDarkBorder
)

private val LightColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = PrimaryBlueText,
    primaryContainer = PrimaryBlueContainer,
    onPrimaryContainer = OnPrimaryBlueContainer,
    secondary = TunisiaRed,
    onSecondary = Color.White,
    tertiary = GoldAccent,
    onTertiary = Color.Black,
    background = ElegantDarkBackground,
    onBackground = TextPrimaryDark,
    surface = ElegantDarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = ElegantDarkSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    outline = ElegantDarkBorder
)

@Composable
fun TakwiraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our tuned vibrant sports scheme for consistency
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

