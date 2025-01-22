package com.defined.mobile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

// Profile screen with a column of custom buttons
@Composable
fun ProfileScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        CustomButtonWithIconAndTriangle(
            text = "Profile Information",
            icon = androidx.compose.material.icons.Icons.Default.Person,
            onClick = { navController.navigate("profileInformation") }
        )
        CustomButtonWithIconAndTriangle(
            text = "Preferences",
            icon = androidx.compose.material.icons.Icons.Default.Settings,
            onClick = { navController.navigate("Preferences") }
        )
        CustomButtonWithIconAndTriangle(
            text = "Allergies",
            icon = androidx.compose.material.icons.Icons.Default.Warning,
            onClick = { }
        )
        CustomButtonWithIconAndTriangle(
            text = "Saved Recipes",
            icon = androidx.compose.material.icons.Icons.Default.ThumbUp, // bookmark doesnt exist
            onClick = { }
        )
        CustomButtonWithIconAndTriangle(
            text = "Liked Recipes",
            icon = androidx.compose.material.icons.Icons.Default.Favorite,
            onClick = { }
        )
    }
}

@Composable
fun CustomButtonWithIconAndTriangle(text: String, icon: ImageVector, onClick: () -> Unit) {
    val blueColor = Color.Blue

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp)
            .padding(vertical = 8.dp)
            .height(60.dp)
            .background(blueColor, shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
            .border(BorderStroke(2.dp, blueColor), shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
            .clickable(onClick = onClick), // Make the entire button clickable
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left icon and text
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = 16.dp), // Padding to shift content away from the edge
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "$text Icon",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = Color.White
            )
        }

        // Triangle icon on the right
        TriangleIcon(theColor = blueColor)
    }
}

@Composable
fun TriangleIcon(theColor: Color) {
    Canvas(
        modifier = Modifier
            .width(30.dp)
            .fillMaxHeight()
            .padding(end = 16.dp)
            .background(theColor)
    ) {
        val path = androidx.compose.ui.graphics.Path().apply {
            moveTo(0f, size.height / 4)
            lineTo(size.width, size.height / 2)
            lineTo(0f, 3 * size.height / 4)
            close()
        }
        drawPath(path, color = Color.White)
    }
}
