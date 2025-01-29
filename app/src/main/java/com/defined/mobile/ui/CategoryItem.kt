package com.defined.mobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.defined.mobile.R

@Composable
fun CategoryItem(name: String) {
    // Card component to contain the category item with padding and rounded corners
    Card(
        modifier = Modifier
            .size(110.dp) // Setting the card size
            .padding(6.dp), // Padding around the card
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer // Card background color
        ),
        shape = RoundedCornerShape(16.dp), // Rounded corners for a softer, modern look
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp) // Elevation for shadow effect
    ) {
        // Column to arrange components vertically inside the card
        Column(
            modifier = Modifier
                .fillMaxSize() // Occupies full card size
                .padding(8.dp), // Padding inside the column
            horizontalAlignment = Alignment.CenterHorizontally, // Center-align horizontally
            verticalArrangement = Arrangement.Center // Center content vertically for balanced look
        ) {
            // Box for circular background behind the image
            Box(
                modifier = Modifier
                    .size(60.dp) // Background size
                    .clip(CircleShape) // Clips the box to a circular shape
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)), // Light background color with transparency
                contentAlignment = Alignment.Center // Centers image inside the box
            ) {
                // Image inside the box with circular clipping
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground), // Image resource
                    contentDescription = name, // Content description for accessibility
                    contentScale = ContentScale.Crop, // Crop image to fit dimensions
                    modifier = Modifier
                        .size(48.dp) // Size of the image
                        .clip(CircleShape) // Clips image to a circular shape for a modern look
                )
            }

            Spacer(modifier = Modifier.height(8.dp)) // Spacer to add space between image and text

            // Text component to display the category name
            Text(
                text = name,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold, // Semi-bold font for emphasis
                    color = MaterialTheme.colorScheme.onSecondaryContainer // Text color for readability
                ),
                fontSize = 14.sp, // Font size of the text
                maxLines = 1 // Restricts text to one line for cleaner look
            )
        }
    }
}
