package com.defined.mobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.defined.mobile.R
import com.defined.mobile.ui.theme.StyledButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikedRecipePage(backActive: Boolean, onBackClick: () -> Unit) {
    // Updated list of recipes with preparation time
    var savedRecipes = listOf(
        DummyRecipe("Chocolate Cake", listOf("Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"), listOf("Dessert"), 45),
        DummyRecipe("Apple Pie", listOf("Apples", "Flour", "Sugar", "Butter", "Cinnamon"), listOf("Dessert"), 60),
        DummyRecipe("Banana Bread", listOf("Bananas", "Flour", "Sugar", "Butter", "Eggs"), listOf("Dessert"), 50),
        DummyRecipe("Pancakes", listOf("Flour", "Milk", "Eggs", "Butter", "Sugar"), listOf("Breakfast"), 15),
        DummyRecipe("Waffles", listOf("Flour", "Milk", "Eggs", "Butter", "Sugar"), listOf("Breakfast"), 20),
        DummyRecipe("Muffins", listOf("Flour", "Sugar", "Butter", "Eggs", "Baking Powder"), listOf("Dessert"), 30),
        DummyRecipe("Brownies", listOf("Flour", "Sugar", "Cocoa Powder", "Butter", "Eggs"), listOf("Dessert"), 35),
        DummyRecipe("Cheesecake", listOf("Cream Cheese", "Sugar", "Butter", "Eggs", "Vanilla Extract"), listOf("Dessert"), 90),
        DummyRecipe("Cookies", listOf("Flour", "Sugar", "Butter", "Eggs", "Chocolate Chips"), listOf("Snack"), 25),
        DummyRecipe("Lemon Tart", listOf("Flour", "Sugar", "Lemons", "Butter", "Eggs"), listOf("Dessert"), 40),
        DummyRecipe("Some Delicious Food", listOf("Flour", "Sugar", "Lemons", "Butter", "Eggs"), listOf("Dessert"), 40),
        DummyRecipe("BBQ Chicken", listOf("Chicken breasts", "Salt", "BBQ sauce"), listOf("Main Dish", "Dinner", "Lunch"), 50)
    )
    var isModified by remember { mutableStateOf(false) }

// States for search query, filters, and expanded views
    var searchQuery by remember { mutableStateOf("") }
    var filteredRecipes by remember { mutableStateOf(savedRecipes) }
    var isFilterSectionVisible by remember { mutableStateOf(false) }
    var isMealTypeVisible by remember { mutableStateOf(false) }
    var isIngredientsVisible by remember { mutableStateOf(false) }
    var selectedMealTypes by remember { mutableStateOf(mutableSetOf<String>()) }
    var selectedIngredientFilter by remember { mutableStateOf("Any ingredient") }
    var selectedSortOption by remember { mutableStateOf("None") }
    var isSortDropdownExpanded by remember { mutableStateOf(false) }

    // TODO: Extend the sort and filter options
    val mealTypes = listOf("Breakfast", "Lunch", "Dinner", "Dessert", "Snack")
    val ingredientFilters = listOf("Only with my ingredients", "Any ingredient")
    val sortOptions = listOf("None", "Preparation Time (Ascending)", "Preparation Time (Descending)")


    // Function to apply filters
    fun applyFilters(recipes: List<DummyRecipe>): List<DummyRecipe> {
        return recipes.filter { recipe ->
            val matchesSearchQuery = searchQuery.isBlank() || recipe.name.contains(searchQuery, ignoreCase = true)
            val matchesMealType = selectedMealTypes.isEmpty() || selectedMealTypes.any { it in recipe.mealType }
            val matchesIngredientFilter = when (selectedIngredientFilter) {
                "Only with my ingredients" -> recipe.ingredients.containsAll(listOf("Flour", "Sugar")) // TODO: Fix this part after inventory is added
                else -> true
            }
            matchesSearchQuery && matchesMealType && matchesIngredientFilter
        }
    }

    // Function to apply sort
    fun applySort(recipes: List<DummyRecipe>): List<DummyRecipe> {
        return when (selectedSortOption) {
            "Preparation Time (Ascending)" -> recipes.sortedBy { it.prepTime }
            "Preparation Time (Descending)" -> recipes.sortedByDescending { it.prepTime }
            else -> recipes
        }
    }

    // Function to update filtered and sorted list
    fun updateFilteredRecipes() {
        val filtered = applyFilters(savedRecipes)
        filteredRecipes = applySort(filtered)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (backActive) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
                Text(
                    text = "Liked Recipes",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    updateFilteredRecipes()
                },
                placeholder = { Text("Search...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors()
            )

            // Row for Filter and Sort Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Filter Button
                StyledButton(
                    text = "Filter",
                    onClick = { isFilterSectionVisible = !isFilterSectionVisible },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Sort Button
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    StyledButton(
                        text = "Sort By",
                        onClick = { isSortDropdownExpanded = true }
                    )
                    DropdownMenu(
                        expanded = isSortDropdownExpanded,
                        onDismissRequest = { isSortDropdownExpanded = false }
                    ) {
                        sortOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedSortOption = option
                                    isSortDropdownExpanded = false
                                    updateFilteredRecipes()
                                }
                            )
                        }
                    }
                }
            }

            // Filter Section
            if (isFilterSectionVisible) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    // Meal Type Button
                    StyledButton(
                        text = "Meal Type",
                        onClick = {
                            isMealTypeVisible = !isMealTypeVisible
                            isIngredientsVisible = false // Close the other section
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (isMealTypeVisible) {
                        Column {
                            mealTypes.forEach { mealType ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = selectedMealTypes.contains(mealType),
                                        onCheckedChange = {
                                            if (it) selectedMealTypes.add(mealType)
                                            else selectedMealTypes.remove(mealType)
                                            updateFilteredRecipes()
                                        }
                                    )
                                    Text(mealType, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Ingredients Button
                    StyledButton(
                        text = "Ingredients",
                        onClick = {
                            isIngredientsVisible = !isIngredientsVisible
                            isMealTypeVisible = false // Close the other section
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (isIngredientsVisible) {
                        Column {
                            ingredientFilters.forEach { option ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = selectedIngredientFilter == option,
                                        onClick = {
                                            selectedIngredientFilter = option
                                            updateFilteredRecipes()
                                        }
                                    )
                                    Text(option, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }

            //TODO: Make the list scrollable

            // Recipe List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredRecipes.take(5)) { recipe ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(recipe.name, style = MaterialTheme.typography.bodyLarge)
                            Text("Meal: ${recipe.mealType.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium)
                            Text("Preparation Time: ${recipe.prepTime} mins", style = MaterialTheme.typography.bodySmall)
                            Text("Ingredients: ${recipe.ingredients.joinToString(", ")}", style = MaterialTheme.typography.bodySmall)
                        }

                        IconButton(onClick = {
                            savedRecipes = savedRecipes.filter { it != recipe }
                            updateFilteredRecipes()
                            isModified = true
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription ="Delete Recipe",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }

        // Save Button
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            Button(
                onClick = {
                    println("Saved Changes")
                    isModified = false
                },
                enabled = isModified,
                modifier = Modifier.padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isModified) MaterialTheme.colorScheme.primary else Color.Gray
                )
            ) {
                Text("Save")
            }
        }
    }
}
