package com.defined.mobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.defined.mobile.R
import com.defined.mobile.backend.RecipeViewModel
import com.defined.mobile.entities.Recipe
import com.defined.mobile.ui.theme.DeleteButton
import com.defined.mobile.ui.theme.fontSmall
import java.util.Locale

@Composable
fun RecipeItem(
    recipe: Recipe,
    onClick: () -> Unit = {},
    deleteActive: Boolean = false,
    recipeViewModel: RecipeViewModel,
    deleteOnClick: () -> Unit = {}
) {
    // 2) Başlangıçta modeldeki TotalTime
    var displayedTime by remember { mutableStateOf(recipe.TotalTime) }

    // 3) recipeCard event’ini al
    val recipeCard by recipeViewModel.recipeCard.collectAsState()

    // 4) Eğer gelen card bizim recipe.ID’e aitse, displayedTime’ı güncelle
    LaunchedEffect(recipeCard) {
        recipeCard?.let { card ->
            val id   = (card["recipe_id"] as? Number)?.toInt() ?: return@LaunchedEffect
            val time = (card["total_time"] as? Number)?.toFloat() ?: return@LaunchedEffect
            if (id == recipe.ID) {
                displayedTime = time
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable {
                // 5) tıklayınca önce backend çağrısı
                recipeViewModel.fetchRecipeCard(
                    recipeId = recipe.ID,
                    fields   = listOf("total_time")
                )
                // sonra dışarıya bildir
                onClick()
            },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            // … thumbnail, title vs aynı …

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recipe.Name ?: "Unnamed Recipe",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                // 6) Burada artık recipe.TotalTime değil displayedTime kullanıyoruz
                Text(
                    text = "${displayedTime.toInt()} minutes",
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

            if (deleteActive) {
                Spacer(modifier = Modifier.width(8.dp))
                DeleteButton(
                    onClick = deleteOnClick,
                    contentDescription = "Delete ${recipe.Name}"
                )
            }
        }
    }
}
