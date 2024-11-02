package com.defined.mobile.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Profile screen with a column of custom buttons
@Composable
fun ProfileScreen() {
    // Column layout for buttons, centered both vertically and horizontally
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        CustomButtonWithTriangle(text = "1", onClick = { })
        CustomButtonWithTriangle(text = "2", onClick = { })
        CustomButtonWithTriangle(text = "3", onClick = { })
        CustomButtonWithTriangle(text = "4", onClick = { })
        CustomButtonWithTriangle(text = "5", onClick = { })
        CustomButtonWithTriangle(text = "6", onClick = { })
        CustomButtonWithTriangle(text = "7", onClick = { })
        CustomButtonWithTriangle(text = "8", onClick = { })
    }
}

// Custom button with a triangle icon on the right side
@Composable
fun CustomButtonWithTriangle(text: String, onClick: () -> Unit) {
    val blueColor = Color.Blue // Define button background color

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp) // Add padding around each button
            .height(60.dp) // Set the button height
            .background(blueColor, shape = RoundedCornerShape(12.dp)) // Rounded blue background
            .border(BorderStroke(2.dp, blueColor), shape = RoundedCornerShape(12.dp)), // Blue border
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Button with text, located on the left side
        ButtonContent(text = text, onClick = onClick, theColor = blueColor, modifier = Modifier.weight(1f) // Fills available horizontal space
            .fillMaxHeight())
        // NOTE: if you give Modifier as parameter and call .weight and .fillMaxHeight methods in function, it will give error.

        // Smaller white triangle on the right side of the button
        TriangleIcon(theColor = blueColor)
    }
}

// Left side button content with customizable text
@Composable
fun ButtonContent(text: String, onClick: () -> Unit, theColor: Color, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier, // Matches parent height
        shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp), // Rounded corners on the left side
        contentPadding = PaddingValues(horizontal = 16.dp), // Padding inside the button
        colors = ButtonDefaults.buttonColors(containerColor = theColor) // Background color
    ) {
        Text(text = text) // Display the button text
    }
}

// Right side triangle icon
@Composable
fun TriangleIcon(theColor: Color) {
    Canvas(
        modifier = Modifier
            .width(30.dp) // Triangle width
            .fillMaxHeight() // Matches parent height
            .padding(end = 16.dp) // Padding on the right
            .background(theColor) // Matches button background color
    ) {
        // Define a triangle path to draw within the canvas
        val path = androidx.compose.ui.graphics.Path().apply {
            moveTo(0f, size.height / 3) // Start point (top-left of triangle)
            lineTo(size.width, size.height / 2) // Middle of the triangle
            lineTo(0f, 2 * size.height / 3) // Bottom point of the triangle
            close() // Close path to create triangle shape
        }
        drawPath(path, color = Color.White) // Draw white triangle on blue background
    }
}
