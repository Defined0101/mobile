package com.defined.mobile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientSearch(
    navController: NavController, // NavController for navigation
    onNavigateBack: () -> Unit // Callback for back navigation
) {
    var searchQuery by remember { mutableStateOf("") }
    val allIngredients = listOf(
        Ingredients("Peanuts"),
        Ingredients("Shellfish"),
        Ingredients("Soy"),
        Ingredients("Eggs"),
        Ingredients("Wheat"),
        Ingredients("Milk")
    )

    val filteredIngredients = allIngredients.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Search Ingredients",
                style = MaterialTheme.typography.titleLarge
            )
        }

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
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredIngredients) { ingredient ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.previousBackStackEntry?.savedStateHandle?.set(
                                "selectedIngredient",
                                ingredient.name
                            )
                            navController.popBackStack()
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = ingredient.name,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

