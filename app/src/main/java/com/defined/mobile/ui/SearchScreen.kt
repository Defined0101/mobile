package com.defined.mobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
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
    // Sample data: List of recipes
    val recipes = listOf(
        DummyRecipe("Chocolate Cake", listOf("Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"), listOf("Dessert")),
        DummyRecipe("Apple Pie", listOf("Apples", "Flour", "Sugar", "Butter", "Cinnamon"), listOf("Dessert")),
        DummyRecipe("Banana Bread", listOf("Bananas", "Flour", "Sugar", "Butter", "Eggs"), listOf("Dessert")),
        DummyRecipe("Pancakes", listOf("Flour", "Milk", "Eggs", "Butter", "Sugar"), listOf("Breakfast")),
        DummyRecipe("Waffles", listOf("Flour", "Milk", "Eggs", "Butter", "Sugar"), listOf("Breakfast")),
        DummyRecipe("Muffins", listOf("Flour", "Sugar", "Butter", "Eggs", "Baking Powder"), listOf("Dessert")),
        DummyRecipe("Brownies", listOf("Flour", "Sugar", "Cocoa Powder", "Butter", "Eggs"), listOf("Dessert")),
        DummyRecipe("Cheesecake", listOf("Cream Cheese", "Sugar", "Butter", "Eggs", "Vanilla Extract"), listOf("Dessert")),
        DummyRecipe("Cookies", listOf("Flour", "Sugar", "Butter", "Eggs", "Chocolate Chips"), listOf("Snack")),
        DummyRecipe("Lemon Tart", listOf("Flour", "Sugar", "Lemons", "Butter", "Eggs"), listOf("Dessert")),
        DummyRecipe("BBQ Chicken", listOf("Chicken breasts", "Salt", "BBQ sauce"), listOf("Main Dish", "Dinner", "Lunch"))
    )

    // Mutable states to handle search query, meal type filter, and filtered list
    var searchQuery by remember { mutableStateOf("") }
    var filteredRecipes by remember { mutableStateOf(recipes) }
    var selectedMealType by remember { mutableStateOf("All") }
    val mealTypes = listOf("All", "Breakfast", "Lunch", "Dinner", "Dessert", "Snack")
    var isDropdownExpanded by remember { mutableStateOf(false) }

    // Function to apply both search and meal type filters
    fun applyFilters() {
        filteredRecipes = recipes.filter { recipe ->
            val matchesSearchQuery = searchQuery.isBlank() || recipe.name.contains(searchQuery, ignoreCase = true) ||
                    recipe.ingredients.any { it.contains(searchQuery, ignoreCase = true) }
            val matchesMealType = selectedMealType == "All" || recipe.mealType.contains(selectedMealType)
            matchesSearchQuery && matchesMealType
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

        // Column layout to arrange elements vertically
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // "Go Back" button
            StyledButton(
                text = "Go Back",
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 16.dp)
            )

            // Search bar for filtering by name or ingredients
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    applyFilters()
                },
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

            Spacer(modifier = Modifier.height(16.dp))

            // Dropdown for meal type selection
            Box {
                StyledButton(
                    text = "Meal Type: $selectedMealType",
                    onClick = { isDropdownExpanded = true }
                )
                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    mealTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                selectedMealType = type
                                isDropdownExpanded = false
                                applyFilters()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display filtered recipes in a LazyColumn
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            )  {
                items(filteredRecipes) { recipe ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = recipe.name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Meal: ${recipe.mealType.joinToString(", ")}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "Ingredients: ${recipe.ingredients.joinToString(", ")}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}