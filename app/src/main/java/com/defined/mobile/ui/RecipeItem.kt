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
import com.defined.mobile.ui.theme.DeleteButton
import com.defined.mobile.ui.theme.fontSmall
import java.util.Locale

@Composable
fun RecipeItem(
    recipe: Recipe,
    onClick: () -> Unit = {},       // Optional tap on whole card
    deleteActive: Boolean = false,   // Show delete button?
    deleteOnClick: () -> Unit = {}   // Delete button action
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            // Thumbnail
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = recipe.Name ?: "Recipe image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.small)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Text content
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = recipe.Name ?: "Unnamed Recipe",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${recipe.TotalTime.toInt()} minutes",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = fontSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    ),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(2.dp))

                val refactoredPreferences = refactorDietPreferences(filterDietPreferences(recipe.Label))
                Text(
                    text = refactoredPreferences.joinToString(", "),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = fontSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    ),
                    maxLines = 2
                )
            }

            // Delete button at end, only if active
            if (deleteActive) {
                Spacer(modifier = Modifier.width(8.dp))
                DeleteButton(
                    onClick = { deleteOnClick() },
                    contentDescription = "Delete ${recipe.Name ?: "recipe"}"
                )
            }
        }
    }
}
