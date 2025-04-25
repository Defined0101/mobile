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
import com.defined.mobile.backend.DislikedRecipeViewModel
import com.defined.mobile.backend.RecipeViewModel
import com.defined.mobile.entities.Recipe
import com.defined.mobile.ui.theme.BackButton
import com.defined.mobile.ui.theme.StyledButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DislikedRecipePage(
    userId: String,
    navController: NavController,
    backActive: Boolean,
    onBackClick: () -> Unit,
    dislikedRecipeViewModel: DislikedRecipeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    recipeViewModel: RecipeViewModel
) {
    // Tüm tarifler (disliked tariflerin listesi; örneğin backend'den gelen liste içerisinde filtre uygulayabilirsiniz)
    val recipesVal by dislikedRecipeViewModel.dislikedRecipes.collectAsState()

    // SearchScreen ile uyumlu filtre ve sıralama state’leri:
    var searchQuery by remember { mutableStateOf("") }
    var isFilterPopupVisible by remember { mutableStateOf(false) }
    var selectedSortOption by remember { mutableStateOf("None") }
    val sortOptions = listOf("None", "Preparation Time (Ascending)", "Preparation Time (Descending)")

    // Çoklu kategori seçimi için state (SearchScreen ile aynı)
    val selectedCategories = remember { mutableStateListOf<String>() }
    // Çoklu preference seçimi için state (SearchScreen ile aynı)
    val selectedPreferences = remember { mutableStateListOf<String>() }

    fun toggleCategory(cat: String) {
        if (selectedCategories.contains(cat)) selectedCategories.remove(cat) else selectedCategories.add(cat)
    }
    fun togglePreference(pref: String) {
        if (selectedPreferences.contains(pref)) selectedPreferences.remove(pref) else selectedPreferences.add(pref)
    }
    fun clearFilters() {
        selectedCategories.clear()
        selectedPreferences.clear()
    }

    // Lokal filtreleme & sıralama SearchScreen ile aynı mantıkta:
    val displayedRecipes = remember(
        recipesVal,
        searchQuery,
        selectedCategories.toList(),
        selectedPreferences.toList(),
        selectedSortOption
    ) {
        recipesVal.filter { recipe ->
            // Arama filtresi: boşsa veya tarif ismi içeriyorsa
            (searchQuery.isEmpty() || recipe.Name.contains(searchQuery, ignoreCase = true)) &&
                    // Kategori filtresi: seçili kategori varsa tarifin kategorisi listede yer almalı
                    (selectedCategories.isEmpty() || selectedCategories.contains(recipe.Category)) &&
                    // Preference filtresi: seçili tüm tercihler tarifin etiketlerinde yer almalı

                    (selectedPreferences.isEmpty() || selectedPreferences.all { pref ->
                        recipe.Label?.contains(pref) ?: false
                    })
        }.let { list ->
            when (selectedSortOption) {
                "Preparation Time (Ascending)" -> list.sortedBy { it.TotalTime }
                "Preparation Time (Descending)" -> list.sortedByDescending { it.TotalTime }
                else -> list.sortedBy { it.ID }
            }
        }
    }

    LaunchedEffect(userId) {
        dislikedRecipeViewModel.fetchDislikedRecipes(userId)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Üst bölüm: Geri butonu ve sayfa başlığı
        ScreenHeader(
            title = "Disliked Recipes",
            onNavigateBack = onBackClick
        )

        // Arama Çubuğu
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search...") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors()
        )
        // Filter ve Sort Row: SearchScreen ile aynı
        FilterAndSortRow(
            selectedSortOption = selectedSortOption,
            onSortSelected = { selectedSortOption = it },
            sortOptions = sortOptions,
            onFilterClick = { isFilterPopupVisible = true }
        )
        // Filter Popup: SearchScreen ile uyumlu (örnek kategori & preference listeleri kullanılıyor; dilersen ViewModel verisi ile değiştirebilirsin)
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
        // Tarif listesi (filtrelenmiş ve sıralanmış)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(displayedRecipes) { _, recipe ->
                RecipeItem(
                    recipe = recipe,
                    onClick = { navController.navigate("recipePage/${recipe.ID}") },
                    deleteActive = true,
                    deleteOnClick = { dislikedRecipeViewModel.removeDislikedRecipe(userId, recipe) },
                    recipeViewModel = recipeViewModel
                )
            }
        }
    }
}
