package com.gourmet.pizzeriaapp.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val PizzeriaColorScheme = lightColorScheme(
    primary = PizzaRed,
    onPrimary = SurfaceCard,
    primaryContainer = PizzaRedLight,
    onPrimaryContainer = PizzaRedDark,
    secondary = FreshGreen,
    onSecondary = SurfaceCard,
    secondaryContainer = FreshGreenLight,
    onSecondaryContainer = FreshGreen,
    tertiary = ForestGreen,
    onTertiary = SurfaceCard,
    background = CreamBackground,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceBeige,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceBeige
)

@Composable
fun PizzeriaAppTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = SurfaceCard.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
            }
        }
    }

    MaterialTheme(
        colorScheme = PizzeriaColorScheme,
        typography = Typography,
        content = content
    )
}
