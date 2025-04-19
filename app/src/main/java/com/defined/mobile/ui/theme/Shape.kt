package com.defined.mobile.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shape = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),  // For more prominent elements
    extraLarge = RoundedCornerShape(30.dp)
)

val fullyRounded = RoundedCornerShape(50)
val endRoundedShape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp) // Only end sides rounded
val circle = CircleShape