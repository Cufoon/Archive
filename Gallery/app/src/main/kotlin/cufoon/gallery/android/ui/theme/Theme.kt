package cufoon.gallery.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = ColorDarkPrimary,
    primaryVariant = ColorDarkPrimaryVariant,
    secondary = ColorDarkSecondary,
    secondaryVariant = ColorDarkSecondaryVariant,
    background = ColorDarkBackground,
    surface = ColorDarkSurface,
    error = ColorDarkError,
    onPrimary = ColorDarkOnPrimary,
    onSecondary = ColorDarkOnSecondary,
    onBackground = ColorDarkOnBackground,
    onSurface = ColorDarkOnSurface,
    onError = ColorDarkOnError
)

private val LightColorPalette = lightColors(
    primary = ColorLightPrimary,
    primaryVariant = ColorLightPrimaryVariant,
    secondary = ColorLightSecondary,
    secondaryVariant = ColorLightSecondaryVariant,
    background = ColorLightBackground,
    surface = ColorLightSurface,
    error = ColorLightError,
    onPrimary = ColorLightOnPrimary,
    onSecondary = ColorLightOnSecondary,
    onBackground = ColorLightOnBackground,
    onSurface = ColorLightOnSurface,
    onError = ColorLightOnError
)

@Composable
fun CufoonGalleryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}