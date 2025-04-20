package com.defined.mobile.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.defined.mobile.backend.SavedRecipeViewModel
import com.defined.mobile.ui.theme.BackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedRecipePage(
    userId: String,
    navController: NavController,
    onBackClick: () -> Unit,
    savedRecipeViewModel: SavedRecipeViewModel,
) {
    // 1) Collect the live list of saved recipes
    val savedRecipes by savedRecipeViewModel.savedRecipes.collectAsState()

    // 2) Search / filter / sort state
    var searchQuery by remember { mutableStateOf("") }
    var isFilterPopupVisible by remember { mutableStateOf(false) }
    var selectedSortOption by remember { mutableStateOf("None") }
    val sortOptions = listOf("None", "Preparation Time (Ascending)", "Preparation Time (Descending)")

    val selectedCategories = remember { mutableStateListOf<String>() }
    val selectedPreferences = remember { mutableStateListOf<String>() }

    fun toggleCategory(cat: String) {
        if (selectedCategories.contains(cat)) selectedCategories.remove(cat)
        else selectedCategories.add(cat)
    }
    fun togglePreference(pref: String) {
        if (selectedPreferences.contains(pref)) selectedPreferences.remove(pref)
        else selectedPreferences.add(pref)
    }
    fun clearFilters() {
        selectedCategories.clear()
        selectedPreferences.clear()
    }

    // 3) Filter & sort in one go
    val displayedRecipes by remember(
        savedRecipes,
        searchQuery,
        selectedCategories.toList(),
        selectedPreferences.toList(),
        selectedSortOption
    ) {
        derivedStateOf {
            savedRecipes
                .filter { recipe ->
                    // search
                    (searchQuery.isEmpty() || recipe.Name.contains(searchQuery, ignoreCase = true)) &&
                            // category
                            (selectedCategories.isEmpty() || selectedCategories.contains(recipe.Category)) &&
                            // preferences
                            (selectedPreferences.isEmpty() || selectedPreferences.all { pref ->
                                recipe.Label.contains(pref)
                            })
                }
                .let { list ->
                    when (selectedSortOption) {
                        "Preparation Time (Ascending)" -> list.sortedBy { it.TotalTime }
                        "Preparation Time (Descending)" -> list.sortedByDescending { it.TotalTime }
                        else -> list
                    }
                }
        }
    }

    // 4) Load saved recipes whenever the userId changes
    LaunchedEffect(userId) {
        savedRecipeViewModel.fetchSavedRecipes(userId)
    }

    // --- UI ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BackButton(onBackClick)
            Text(
                text = "Saved Recipes",
                style = MaterialTheme.typography.titleLarge
            )
        }

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors()
        )

        // Filter & Sort Row
        FilterAndSortRow(
            selectedSortOption = selectedSortOption,
            onSortSelected = { selectedSortOption = it },
            sortOptions = sortOptions,
            onFilterClick = { isFilterPopupVisible = true }
        )

        // Filter Popup
        FilterPopup(
            isVisible = isFilterPopupVisible,
            onDismiss = { isFilterPopupVisible = false },
            onClearFilters = { clearFilters() },
            categories = listOf("Breakfast", "Lunch", "Dinner", "Dessert", "Snack"),
            selectedCategories = selectedCategories,
            onCategoryToggle = { toggleCategory(it) },
            preferences = listOf("vegan", "vegetarian", "gluten_free", "dairy_free", "pescetarian"),
            selectedPreferences = selectedPreferences,
            onPreferenceToggle = { togglePreference(it) }
        )

        // List of recipes
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(displayedRecipes) { _, recipe ->
                RecipeItem(
                    recipe = recipe,
                    onClick = { navController.navigate("recipePage/${recipe.ID}") },
                    deleteActive = true,
                    deleteOnClick = { savedRecipeViewModel.removeSavedRecipe(userId, recipe) }
                )
            }
        }
    }
}
