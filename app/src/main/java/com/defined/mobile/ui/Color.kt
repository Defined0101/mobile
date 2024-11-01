package com.defined.mobile.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Updated Light Color Scheme
val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),      // Elegant purple for primary
    onPrimary = Color.White,
    secondary = Color(0xFF03DAC5),    // Soft teal for secondary
    onSecondary = Color.Black,
    background = Color(0xFFF5F5F5),   // Light grey background for comfort
    onBackground = Color.Black,
    surface = Color(0xFFFFFFFF),      // Pure white surface for cards, etc.
    onSurface = Color.Black,
    error = Color(0xFFB00020),        // Material Design error color
    onError = Color.White
)

// Updated Dark Color Scheme
val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),      // Softer purple for dark mode primary
    onPrimary = Color.Black,
    secondary = Color(0xFF03DAC5),    // Teal also works great on dark background
    onSecondary = Color.White,
    background = Color(0xFF121212),   // Dark gray for main background
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),      // Dark surface for cards
    onSurface = Color.White,
    error = Color(0xFFCF6679),        // Softer red for errors in dark mode
    onError = Color.Black
)
