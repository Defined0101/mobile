package com.defined.mobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.defined.mobile.R
import com.defined.mobile.entities.Recipe
import com.defined.mobile.ui.theme.*
import java.util.Locale

@Composable
fun RecipeItem(
    recipe: Recipe,
    onClick: () -> Unit = {}, // Optional onClick action, defaults to no action

) {
    // Card container for the recipe item
    Card(
        modifier = Modifier
            .fillMaxWidth() // Card takes full width of its container
            .padding(
                vertical = 4.dp,
                horizontal = 8.dp
            ) // Adds vertical and horizontal padding around the card
            .clickable { onClick() }, // Makes the card clickable
        shape = MaterialTheme.shapes.medium, // Rounded corners for a modern appearance
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp) // Adds shadow for a subtle elevation effect
    ) {
        // Row layout to display image and text side-by-side
        Row(
            verticalAlignment = Alignment.CenterVertically, // Center-aligns content vertically
            modifier = Modifier.padding(12.dp) // Padding around row content for spacing
        ) {
            // Recipe Image with rounded corners
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = recipe.Name, // Accessibility description
                contentScale = ContentScale.Crop, // Scales image to fill space without distortion
                modifier = Modifier
                    .size(80.dp) // Fixed size for image
                    .clip(MaterialTheme.shapes.small) // Rounded edges for the image
            )

            Spacer(modifier = Modifier.width(12.dp)) // Spacer to add horizontal space between image and text

            // Column layout for the text section
            Column(
                modifier = Modifier.weight(1f) // Occupies remaining horizontal space
            ) {
                // Recipe Title
                Text(
                    text = recipe.Name,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.onSurface // Text color for contrast
                    )
                )

                Spacer(modifier = Modifier.height(2.dp)) // Spacer to add vertical space between title and description

                // Recipe Category
                Text(
                    text = "${recipe.TotalTime.toInt()} minutes",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = fontSmall, // Smaller font size for description
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f) // Slightly transparent color for subdued look
                    ),
                    maxLines = 2 // Limits text to 2 lines to keep layout compact
                )

                Spacer(modifier = Modifier.height(2.dp)) // Spacer to add vertical space between title and description

                // Recipe Description
                val refactoredPreferences = refactorDietPreferences(filterDietPreferences(recipe.Label))
                Text(
                    text = refactoredPreferences.joinToString(", "),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = fontSmall, // Smaller font size for description
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f) // Slightly transparent color for subdued look
                    ),
                    maxLines = 2 // Limits text to 2 lines to keep layout compact
                )
            }
        }
    }
}
