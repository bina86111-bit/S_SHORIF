package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = FireBluePrimary,
    onPrimary = DeepNavyBg,
    primaryContainer = CardNavyElevated,
    onPrimaryContainer = FireBluePrimary,
    secondary = SkyBlueAccent,
    onSecondary = DeepNavyBg,
    secondaryContainer = CardNavy,
    onSecondaryContainer = SkyBlueAccent,
    tertiary = FlameOrange,
    onTertiary = DeepNavyBg,
    background = DeepNavyBg,
    onBackground = TextPrimary,
    surface = CardNavy,
    onSurface = TextPrimary,
    surfaceVariant = CardNavyElevated,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    outlineVariant = BorderGlow,
    error = DangerRed,
    onError = TextPrimary
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit,
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = DeepNavyBg.toArgb()
                window.navigationBarColor = DeepNavyBg.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
