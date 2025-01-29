package com.defined.mobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

//TODO: Connect to database
//TODO: Only one ingredient can be selected. After db connection fix it to multiple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllergyPage(
    navController: NavController, // NavController for navigation
    onNavigateBack: () -> Unit // Callback for back navigation
) {
    val allergicIngredients = remember { mutableStateListOf<Ingredients>() }
    var isModified by remember { mutableStateOf(false) }

    // Observe navigation result
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val selectedIngredient = navBackStackEntry?.savedStateHandle?.get<String>("selectedIngredient")

    // Add the selected ingredient to the list if it doesn't already exist
    LaunchedEffect(selectedIngredient) {
        selectedIngredient?.let { ingredientName ->
            if (allergicIngredients.none { it.name == ingredientName }) {
                allergicIngredients.add(Ingredients(ingredientName))
                isModified = true
            }
        }
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
                text = "Allergies",
                style = MaterialTheme.typography.titleLarge
            )
            IconButton(onClick = {
                navController.navigate("ingredientSearch") // Navigate to IngredientSearch
            }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Ingredient")
            }
        }

        // Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Your Allergies",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
            )
        }

        // List of Allergic Ingredients
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            allergicIngredients.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFF4CAF50), // Green for active allergies
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.name,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.White
                        )
                    )
                    IconButton(
                        onClick = {
                            allergicIngredients.removeAt(index)
                            isModified = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete Allergy",
                            tint = Color.White
                        )
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
                    println("Saved Allergies: ${allergicIngredients.map { it.name }}")
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
