package com.defined.mobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
fun RecipeItem(
    name: String,
    description: String = "A delicious recipe to try!", // Optional description
    onClick: () -> Unit = {} // Optional click action
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp) // Padding around the card for spacing
            .clickable { onClick() }, // Clickable functionality
        shape = RoundedCornerShape(16.dp), // Softer rounded corners for a modern look
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            // Recipe Image
            Image(
                painter = painterResource(id = R.drawable.featured_image),
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)) // Rounded corners for the image
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Text Section
            Column(
                modifier = Modifier.weight(1f) // Take remaining space
            ) {
                // Recipe Title
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Recipe Description
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    ),
                    maxLines = 2 // Limit to 2 lines to keep it compact
                )
            }
        }
    }
}
