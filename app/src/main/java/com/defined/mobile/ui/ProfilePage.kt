package com.defined.mobile.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.defined.mobile.R
import com.defined.mobile.ui.theme.*

// Profile screen with a column of custom buttons
@Composable
fun ProfileScreen(navController: NavHostController, viewModel: LoginViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        CustomButtonWithIconAndTriangle(
            text = "Profile Information",
            icon = Icons.Default.Person,
            onClick = { navController.navigate("profileInformation") }
        )
        CustomButtonWithIconAndTriangle(
            text = "Preferences",
            icon = Icons.Default.Settings,
            onClick = { navController.navigate("preferences") }
        )
        CustomButtonWithIconAndTriangle(
            text = "Inventory",
            icon = Icons.Default.ShoppingCart,
            onClick = { navController.navigate("ingredients") }
        )
        CustomButtonWithIconAndTriangle(
            text = "Allergies",
            icon = Icons.Default.Warning,
            onClick = { navController.navigate("allergies") }
        )
        CustomButtonWithIconAndTriangle(
            text = "Saved Recipes",
            icon = Icons.Default.Star, // was ThumbUp
            onClick = { navController.navigate("savedRecipes") }
        )
        CustomButtonWithIconAndTriangle(
            text = "Liked Recipes",
            icon = Icons.Default.Favorite,
            onClick = { navController.navigate("likedRecipes/true") }
        )
        CustomButtonWithIconAndTriangle(
            text = "Logout",
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            onClick = {
                viewModel.logOut()
                navController.navigate("login") {
                    popUpTo("main") { inclusive = true }
                }
            }
        )
    }


}

@Composable
fun CustomButtonWithIconAndTriangle(text: String, icon: ImageVector, onClick: () -> Unit) {
    val blueColor = MaterialTheme.colorScheme.primary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp)
            .padding(vertical = 8.dp)
            .height(60.dp)
            .background(blueColor, shape = endRoundedShape)
            .border(BorderStroke(2.dp, blueColor), shape = endRoundedShape)
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
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        // Triangle icon on the right
        TriangleIcon(theColor = blueColor)
    }
}

@Composable
fun TriangleIcon(theColor: Color) {
    val triangleColor = MaterialTheme.colorScheme.onPrimary
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
        drawPath(path, color = triangleColor)
    }
}
