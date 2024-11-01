package com.defined.mobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.defined.mobile.R
import com.defined.mobile.ui.theme.StyledButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(onBackClick: () -> Unit) {
    // Sample data: List of recipe names
    // TODO: Dummy data can be altered to include ingredients to test filtering function
    val recipeNames = listOf(
        "Chocolate Cake", "Apple Pie", "Banana Bread",
        "Pancakes", "Waffles", "Muffins", "Brownies",
        "Cheesecake", "Cookies", "Lemon Tart"
    )

    // Mutable states to handle search query and filtered list
    var searchQuery by remember { mutableStateOf("") }
    var filteredRecipes by remember { mutableStateOf(recipeNames) }

    // Filter functionality to sort recipes alphabetically
    // TODO: Add filter options to the function, now this function just sorts data
    fun applyFilter() {
        filteredRecipes = filteredRecipes.sorted()
    }

    // Search functionality to filter recipes based on query
    fun applySearch(query: String) {
        searchQuery = query
        filteredRecipes = if (query.isBlank()) {
            recipeNames // Reset to full list if query is blank
        } else {
            recipeNames.filter { it.contains(query, ignoreCase = true) }
        }
    }

    // Main container with full screen size
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image for consistency with MainPage
        Image(
            painter = painterResource(id = R.drawable.background_image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Column layout to arrange elements vertically on the screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // "Go Back" button, aligned to the start
            StyledButton(
                text = "Go Back",
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 16.dp)
            )

            // Search bar with real-time search functionality
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { applySearch(it) }, // Applies search as user types
                placeholder = {
                    Text(
                        text = "Search...",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surface),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(24.dp)) // Spacer to add space between search bar and buttons

            // Row layout for Filter and Sort buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // "Filter" button to sort recipes alphabetically
                StyledButton(
                    text = "Filter",
                    onClick = { applyFilter() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                // "Sort" button as an example of sorting functionality
                StyledButton(
                    text = "Sort",
                    onClick = {
                        // TODO: Implement sorting function with options
                        applyFilter()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp)) // Spacer to add space between buttons and recipe list

            // Display filtered recipes in a LazyColumn
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Iterate through each filtered recipe
                items(filteredRecipes) { recipe ->
                    // Display each recipe name in a styled Text component
                    Text(
                        text = recipe,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp) // Inner padding for text readability
                    )
                }
            }
        }
    }
}
