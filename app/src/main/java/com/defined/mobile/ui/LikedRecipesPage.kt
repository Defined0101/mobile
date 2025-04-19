package com.defined.mobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.defined.mobile.backend.LikedRecipeViewModel
import com.defined.mobile.backend.RecipeViewModel
import com.defined.mobile.entities.Recipe
import com.defined.mobile.ui.theme.BackButton
import com.defined.mobile.ui.theme.DeleteButton
import com.defined.mobile.ui.theme.SaveButton
import com.defined.mobile.ui.theme.StyledButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikedRecipePage(
    userId: String, // Pass user ID
    navController: NavController,
    backActive: Boolean,
    onBackClick: () -> Unit,
    likedRecipeViewModel: LikedRecipeViewModel,
) {
    val likedRecipesVal by likedRecipeViewModel.likedRecipes.collectAsState()
    //println("LikedRecipePage likedRecipesVal: " + likedRecipesVal)
    //val likedRecipesVal = sharedViewModel.likedRecipes

    // States for search query, filters, and sorting
    var searchQuery by remember { mutableStateOf("") }
    var selectedMealTypes by remember { mutableStateOf(mutableSetOf<String>()) }
    var selectedSortOption by remember { mutableStateOf("None") }
    var isSortDropdownExpanded by remember { mutableStateOf(false) }
    var isFilterSectionVisible by remember { mutableStateOf(false) }
    var isMealTypeVisible by remember { mutableStateOf(false) }

    val mealTypes = listOf("Breakfast", "Lunch", "Dinner", "Dessert", "Snack")
    val sortOptions = listOf("None", "Preparation Time (Ascending)", "Preparation Time (Descending)")

    // Apply filters and sorting dynamically
    val filteredRecipes = likedRecipesVal.filter { recipe ->
        val matchesSearchQuery = searchQuery.isBlank() || recipe.Name.contains(searchQuery, ignoreCase = true)
        val matchesMealType = selectedMealTypes.isEmpty() || selectedMealTypes.any { it in recipe.Label }
        matchesSearchQuery && matchesMealType
    }.let { recipes ->
        when (selectedSortOption) {
            "Preparation Time (Ascending)" -> recipes.sortedBy { it.TotalTime }
            "Preparation Time (Descending)" -> recipes.sortedByDescending { it.TotalTime }
            else -> recipes
        }
    }

    // Fetch liked recipes on userId change
    LaunchedEffect(userId) {
        likedRecipeViewModel.fetchLikedRecipes(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (backActive) {
                BackButton(onBackClick)
            }
            Text(text = "Liked Recipes", style = MaterialTheme.typography.titleLarge)
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search...") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors()
        )

        // Row for Filter and Sort Buttons
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StyledButton(text = "Filter", onClick = { isFilterSectionVisible = !isFilterSectionVisible }, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))

            Box(modifier = Modifier.weight(1f)) {
                StyledButton(text = "Sort", onClick = { isSortDropdownExpanded = true })
                DropdownMenu(expanded = isSortDropdownExpanded, onDismissRequest = { isSortDropdownExpanded = false }) {
                    sortOptions.forEach { option ->
                        DropdownMenuItem(text = { Text(option) }, onClick = {
                            selectedSortOption = option
                            isSortDropdownExpanded = false
                        })
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
                // Meal Type Filter
                StyledButton(text = "Meal Type", onClick = { isMealTypeVisible = !isMealTypeVisible }, modifier = Modifier.fillMaxWidth())
                if (isMealTypeVisible) {
                    Column {
                        mealTypes.forEach { mealType ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = selectedMealTypes.contains(mealType),
                                    onCheckedChange = {
                                        if (it) selectedMealTypes.add(mealType) else selectedMealTypes.remove(mealType)
                                    }
                                )
                                Text(mealType, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }

        // Recipe List
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(filteredRecipes) { index, recipe ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                        .clickable { navController.navigate("recipePage/${recipe.ID}") }, // Use recipe.id instead of index
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(recipe.Name, style = MaterialTheme.typography.bodyLarge)
                        Text("Meal: ${recipe.Label.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium)
                        Text("Preparation Time: ${recipe.TotalTime} mins", style = MaterialTheme.typography.bodySmall)
                        Text("Ingredients: ${recipe.Ingredients.joinToString(", ")}", style = MaterialTheme.typography.bodySmall)
                    }

                    // delete Button (Deletes Recipe from Liked List)
                    DeleteButton(onClick = {
                        likedRecipeViewModel.removeLikedRecipe(userId, recipe) // Properly updates ViewModel
                        //sharedViewModel.unlikeRecipe(recipe.ID)
                    }, contentDescription = "Unlike Recipe")
                }
            }
        }
    }
}


