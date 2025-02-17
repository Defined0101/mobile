package com.defined.mobile.ui

// Your existing imports
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut

import com.defined.mobile.R
import com.defined.mobile.backend.RecipeViewModel
import com.defined.mobile.entities.Recipe
import com.defined.mobile.ui.theme.StyledButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, backActive: Boolean, onBackClick: () -> Unit, viewModel: RecipeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    // Updated list of recipes with preparation time
    val recipesVal by viewModel.recipes.collectAsState()
    var recipes = recipesVal
    // States for search query, filters, and expanded views
    var searchQuery by remember { mutableStateOf("") }
    var filteredRecipes by remember { mutableStateOf(recipes) }
    var isFilterDialogVisible by remember { mutableStateOf(false) }
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
    fun applyFilters(recipes: List<Recipe>): List<Recipe> {
        return recipes.filter { recipe ->
            val matchesSearchQuery = searchQuery.isBlank() || recipe.Name.contains(searchQuery, ignoreCase = true)
            val matchesMealType = selectedMealTypes.isEmpty() || selectedMealTypes.any { it in recipe.Label }
            val matchesIngredientFilter = when (selectedIngredientFilter) {
                "Only with my ingredients" -> true//recipe.Ingredients.containsAll(listOf("Flour", "Sugar")) // TODO: Fix this part after inventory is added
                else -> true
            }
            matchesSearchQuery && matchesMealType && matchesIngredientFilter
        }
    }

    // Function to apply sort
    fun applySort(recipes: List<Recipe>): List<Recipe> {
        return when (selectedSortOption) {
            "Preparation Time (Ascending)" -> recipes.sortedBy { it.TotalTime }
            "Preparation Time (Descending)" -> recipes.sortedByDescending { it.TotalTime }
            else -> recipes
        }
    }
    // Function to update filtered and sorted list
    fun updateFilteredRecipes() {
        val filtered = applyFilters(recipes)
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
            if (backActive) {
                StyledButton(
                    text = "Go Back",
                    onClick = onBackClick,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 16.dp)
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
                    onClick = { isFilterDialogVisible = true },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Sort Button
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    StyledButton(
                        text = "Sort",
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

            // **Modal Filter Dialog**
            if (isFilterDialogVisible) {
                AlertDialog(
                    onDismissRequest = { isFilterDialogVisible = false },
                    confirmButton = {
                        StyledButton(
                            text = "Apply Filters",
                            onClick = {
                                updateFilteredRecipes()
                                isFilterDialogVisible = false
                            }
                        )
                    },
                    dismissButton = {
                        StyledButton(
                            text = "Cancel",
                            onClick = { isFilterDialogVisible = false }
                        )
                    },
                    title = { Text("Filter Recipes") },
                    text = {
                        Column {
                            StyledButton(
                                text = "Meal Type",
                                onClick = {
                                    isMealTypeVisible = !isMealTypeVisible
                                    isIngredientsVisible = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                            // **Meal Type Bölümü**
                            AnimatedVisibility(
                                visible = isMealTypeVisible,
                                enter = fadeIn(animationSpec = tween(300)) + expandVertically(animationSpec = tween(300)),
                                exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(animationSpec = tween(300))
                            ) {
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

                            StyledButton(
                                text = "Ingredients",
                                onClick = {
                                    isIngredientsVisible = !isIngredientsVisible
                                    isMealTypeVisible = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                            // **Ingredients Bölümü**
                            AnimatedVisibility(
                                visible = isIngredientsVisible,
                                enter = fadeIn(animationSpec = tween(300)) + expandVertically(animationSpec = tween(300)),
                                exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(animationSpec = tween(300))
                            ) {
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
                )
            }

            // Recipe List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(filteredRecipes) { index, recipe ->
                    RecipeItem(
                        recipe = recipe,
                        onClick = {
                            navController.navigate("recipePage/$index")
                        }
                    )
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .background(MaterialTheme.colorScheme.surface)
//                            .padding(16.dp)
//                            .clickable {
//                                navController.navigate("recipePage/$index")
//                            }
//                    ) {
//                        Text(recipe.name, style = MaterialTheme.typography.bodyLarge)
//                        Text("Meal: ${recipe.mealType.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium)
//                        Text("Preparation Time: ${recipe.prepTime} mins", style = MaterialTheme.typography.bodySmall)
//                        Text("Ingredients: ${recipe.ingredients.joinToString(", ")}", style = MaterialTheme.typography.bodySmall)
//                    }
                }
            }
        }
    }
}
