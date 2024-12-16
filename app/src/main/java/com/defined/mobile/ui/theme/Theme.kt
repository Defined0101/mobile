package com.defined.mobile.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun MobileTheme(
    darkTheme: Boolean = false, // Boolean to switch between light and dark theme
    content: @Composable () -> Unit // Content block that will inherit the theme styling
) {
    // Selects the color scheme based on the darkTheme parameter
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    // Applies the MaterialTheme to the app's UI elements
    MaterialTheme(
        colorScheme = colorScheme, // Uses chosen color scheme (dark or light)
        typography = Typography,   // Applies app-wide typography styles
        shapes = Shape,            // Applies defined shapes for UI elements
        content = content          // Applies the theme to the provided content
    )
}
