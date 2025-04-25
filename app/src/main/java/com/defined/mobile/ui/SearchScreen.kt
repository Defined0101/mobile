package com.defined.mobile.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Sort
import com.defined.mobile.R
import com.defined.mobile.backend.CategoryViewModel
import com.defined.mobile.backend.PreferencesViewModel
import com.defined.mobile.backend.RecipeViewModel
import com.defined.mobile.backend.ShoppingListViewModel
import com.defined.mobile.entities.Ingredient
import com.defined.mobile.entities.QueryClass
import com.defined.mobile.entities.Recipe
import com.defined.mobile.ui.theme.StyledButton
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    backActive: Boolean,
    onBackClick: () -> Unit,
    // Yeni parametre: başlangıçtaki seçili kategori (MainPage'den gelecek)
    initialSelectedCategory: String = "",
    recipeViewModel: RecipeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    categoryViewModel: CategoryViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    preferencesViewModel: PreferencesViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    shoppingListViewModel: ShoppingListViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // Tüm tarif listesi (backend'den alınan liste)
    val recipesVal by recipeViewModel.recipesSearch.collectAsState()
    val categoriesVal by categoryViewModel.categories.collectAsState()
    val preferencesVal by preferencesViewModel.preferences.collectAsState()


    var textFieldValue by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    var isFilterPopupVisible by remember { mutableStateOf(false) }
    var selectedSortOption by remember { mutableStateOf("None") }
    val sortOptions = listOf("None", "Preparation Time (Ascending)", "Preparation Time (Descending)")

    // Çoklu kategori seçimi için state; başlangıçta initialSelectedCategory boş değilse ekle
    val selectedCategories = remember { mutableStateListOf<String>().apply {
        if(initialSelectedCategory.isNotEmpty()) add(initialSelectedCategory)
    } }
    // Çoklu preference (diyet) seçimi için state
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

    val currentFilter = remember(searchQuery, selectedCategories, selectedPreferences) {
        QueryClass(
            inputText  = searchQuery,
            categories = selectedCategories.toList(),
            labels     = selectedPreferences.toList()
        )
    }
    fun sortField()     = "name"
    fun sortDirection() = when(selectedSortOption) {
        "Preparation Time (Ascending)"  -> "ascending"
        "Preparation Time (Descending)" -> "descending"
        else                             -> "none"
    }

    LaunchedEffect(textFieldValue) {
        delay(3000)
        searchQuery = textFieldValue
    }

    LaunchedEffect(searchQuery, selectedSortOption) {
        // boş sorguyu atlama istersen if(searchQuery.isNotBlank())
        val qc = QueryClass(
            inputText  = searchQuery,
            categories = selectedCategories.toList(),
            labels     = selectedPreferences.toList()
        )
        recipeViewModel.searchRecipes(
            qc,
            sortField(),
            sortDirection()
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenHeader(
            title = "",
            onNavigateBack = onBackClick
        )

        /*
        if (backActive) {
            StyledButton(
                text = "Go Back",
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 16.dp)
            )
        }
         */
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
                },
            placeholder = { Text("Search...") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors()
        )
        FilterAndSortRow(
            selectedSortOption = selectedSortOption,
            onSortSelected = { selectedSortOption = it },
            sortOptions = sortOptions,
            onFilterClick = { isFilterPopupVisible = true }
        )
        FilterPopup(
            isVisible = isFilterPopupVisible,
            onDismiss = { isFilterPopupVisible = false },
            onClearFilters = { clearFilters() },
            categories = categoriesVal,
            selectedCategories = selectedCategories,
            onCategoryToggle = { toggleCategory(it) },
            preferences = preferencesVal,
            selectedPreferences = selectedPreferences,
            onPreferenceToggle = { togglePreference(it) }
        )
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .align(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${recipesVal.size} recipes found",
                style = MaterialTheme.typography.bodyMedium
            )
            // Filtreler boş değilse yanına ek metin
            if (selectedCategories.isNotEmpty() || selectedPreferences.isNotEmpty()) {
                // Virgüllerle ayrılmış String
                val filtersText = (selectedCategories + refactorDietPreferences(selectedPreferences)).joinToString(", ")
                Text(
                    text = " ($filtersText)",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(recipesVal) { recipe ->
                RecipeItem(
                    Recipe(
                        ID           = recipe.ID,
                        Name         = recipe.Name,
                        Ingredients  = recipe.Ingredients.map { name ->
                            Ingredient(name = name)
                        },
                        TotalTime    = recipe.TotalTime,
                        Category     = recipe.Category,
                        Label        = recipe.Label
                    ),
                    recipeViewModel = recipeViewModel,
                    onClick = {
                        navController.navigate("recipePage/${recipe.ID}")
                    }
                )
            }
        }
    }
}

@Composable
fun FilterAndSortRow(
    selectedSortOption: String,
    onSortSelected: (String) -> Unit,
    sortOptions: List<String>,
    onFilterClick: () -> Unit
) {
    var isSortDropdownExpanded by remember { mutableStateOf(false) }
    val sortButtonWidth = remember { mutableStateOf(0) }
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(
            onClick = onFilterClick,
            modifier = Modifier.weight(1f).height(56.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Icon(imageVector = Icons.Default.FilterList, contentDescription = "Filter", modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Filter", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.width(12.dp))
        OutlinedButton(
            onClick = { isSortDropdownExpanded = !isSortDropdownExpanded },
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .onGloballyPositioned { coordinates -> sortButtonWidth.value = coordinates.size.width },
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Icon(imageVector = Icons.Default.Sort, contentDescription = "Sort", modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(selectedSortOption, style = MaterialTheme.typography.bodyLarge)
            DropdownMenu(
                expanded = isSortDropdownExpanded,
                onDismissRequest = { isSortDropdownExpanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { sortButtonWidth.value.toDp() })
                    .background(color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f), shape = MaterialTheme.shapes.extraLarge)
                    .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), shape = MaterialTheme.shapes.extraLarge)
                    .padding(vertical = 6.dp)
            ) {
                sortOptions.forEachIndexed { index, option ->
                    DropdownMenuItem(
                        text = {
                            Text(text = option, style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(start = 16.dp))
                        },
                        onClick = {
                            onSortSelected(option)
                            isSortDropdownExpanded = false
                        },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                    if (index != sortOptions.lastIndex) {
                        Divider(modifier = Modifier.padding(horizontal = 12.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterPopup(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onClearFilters: () -> Unit,
    categories: List<String>,
    selectedCategories: List<String>,
    onCategoryToggle: (String) -> Unit,
    preferences: List<String>,
    selectedPreferences: List<String>,
    onPreferenceToggle: (String) -> Unit
) {
    if (!isVisible) return

    // we’ll need this for the scroll state:
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = { Spacer(modifier = Modifier.height(8.dp)) }
    ) {
        // Wrap in a Box with a height constraint so it never grows past the screen:
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp)       // tweak as needed!
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)  // ← make it scrollable
                    .padding(16.dp)
            ) {
                Text("Filter Options", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))

                // --- Categories ---
                Text("Categories", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                categories.forEach { cat ->
                    val isChecked = cat in selectedCategories
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { onCategoryToggle(cat) }
                        )
                        Text(cat)
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 16.dp))

                // --- Preferences ---
                Text("Preferences", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                val formattedPreferences = refactorDietPreferences(preferences)
                preferences.zip(formattedPreferences).forEach { (pref, displayName) ->
                    val isChecked = pref in selectedPreferences
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { onPreferenceToggle(pref) }
                        )
                        Text(displayName)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = onClearFilters,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Clear Filters")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }
        }
    }
}