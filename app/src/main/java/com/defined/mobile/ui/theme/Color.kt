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
    onError = Color.White,
    secondaryContainer = Color(0xFF424242),
    onSecondaryContainer = Color.White,
    surfaceVariant = Color.Black
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
    onError = Color.Black,
    secondaryContainer = Color(0xFFA4A0A0),
    onSecondaryContainer = Color.Black,
    surfaceVariant = Color.Black
)

// RecipePage.kt colors
// Diet Preference Chip Colors
val VeganColor = Color(0xFFA8E6CF)
val VegetarianColor = Color(0xFFC8E6C9)
val DairyFreeColor = Color(0xFFB3E5FC)
val GlutenFreeColor = Color(0xFFFFE0B2)
val PescetarianColor = Color(0xFFE1BEE7)
val DefaultChipColor = Color(0xFFCFD8DC)
val badgeBackground = Color(0xFFE0F7FA)
val badgeContentColor = Color(0xFF006064)
val softBlue = Color(0xFF3F51B5)

// allergy.kt and preferences.kt
val brightGreen = Color(0xFF4CAF50)
val lightGray = Color(0xFFE0E0E0)

val TransparentColor = Color(0x00000000) // Fully transparent
