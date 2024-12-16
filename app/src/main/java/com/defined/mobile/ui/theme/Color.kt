package com.defined.mobile.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Updated Light Color Scheme
val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),      // Primary color - elegant purple for a bold, modern look
    onPrimary = Color.White,          // Text/icon color on primary - white for good contrast
    secondary = Color(0xFF03DAC5),    // Secondary color - soft teal, adding a fresh accent
    onSecondary = Color.Black,        // Text/icon color on secondary - black for contrast
    background = Color(0xFFF5F5F5),   // Background color - light grey for a comfortable, neutral base
    onBackground = Color.Black,       // Text/icon color on background - black for readability
    surface = Color(0xFFFFFFFF),      // Surface color - pure white for cards and other UI elements
    onSurface = Color.Black,          // Text/icon color on surface - black for contrast
    error = Color(0xFFB00020),        // Error color - standard Material Design red for alerts
    onError = Color.White             // Text/icon color on error - white for clear contrast
)

// Updated Dark Color Scheme
val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),      // Primary color - a softer purple for less eye strain in dark mode
    onPrimary = Color.Black,          // Text/icon color on primary - black for readability on softer purple
    secondary = Color(0xFF03DAC5),    // Secondary color - teal complements the dark background
    onSecondary = Color.White,        // Text/icon color on secondary - white for high contrast
    background = Color(0xFF121212),   // Background color - very dark grey to reduce eye strain
    onBackground = Color.White,       // Text/icon color on background - white for readability
    surface = Color(0xFF1E1E1E),      // Surface color - dark grey for cards and surfaces
    onSurface = Color.White,          // Text/icon color on surface - white for contrast
    error = Color(0xFFCF6679),        // Error color - softer red to prevent harsh contrasts in dark mode
    onError = Color.Black             // Text/icon color on error - black for readability on soft red
)
