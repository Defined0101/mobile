package com.defined.mobile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.defined.mobile.backend.IngredientsViewModel
import com.defined.mobile.entities.Ingredient
import com.defined.mobile.ui.theme.BackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientSearch(
    navController: NavController, // NavController for navigation
    onNavigateBack: () -> Unit, // Callback for back navigation
    ingredientsViewModel: IngredientsViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }

    val ingredients by ingredientsViewModel.ingredients.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(searchQuery) {
        ingredientsViewModel.onSearchChanged(searchQuery)
    }

    LaunchedEffect(listState.firstVisibleItemIndex, listState.layoutInfo.totalItemsCount) {
        val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
        val totalItems = listState.layoutInfo.totalItemsCount

        if (lastVisible >= totalItems - 3) {
            ingredientsViewModel.loadNextPage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Bar
        ScreenHeader(
            title = "Search Ingredients",
            onNavigateBack = onNavigateBack
        )

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search ingredients...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Ingredient List
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ingredients.items) { ingredient ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.previousBackStackEntry?.savedStateHandle?.set(
                                "selectedIngredient",
                                Pair(ingredient.name, ingredient.default_unit)
                            )
                            navController.popBackStack()
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = ingredient.name ?: "Unnamed Ingredient",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

