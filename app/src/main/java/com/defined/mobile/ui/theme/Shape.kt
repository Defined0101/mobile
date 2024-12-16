package com.defined.mobile.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Shape theme with custom rounded corner sizes for UI consistency
val Shape = Shapes(
    small = RoundedCornerShape(8.dp),   // Small rounded corner for minor elements like buttons
    medium = RoundedCornerShape(16.dp), // Medium rounded corner for cards and modals
    large = RoundedCornerShape(24.dp)   // Large rounded corner for prominent elements like dialogs
)
