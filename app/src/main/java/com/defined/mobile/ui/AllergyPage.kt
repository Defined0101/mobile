package com.defined.mobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.defined.mobile.ui.theme.*
import com.defined.mobile.backend.UserAllergiesViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.defined.mobile.entities.UserAllergies

//TODO: Connect to database
//TODO: Only one ingredient can be selected. After db connection fix it to multiple

@Composable
fun AllergyPage(
    userAllergiesViewModel: UserAllergiesViewModel = viewModel(),
    userId: String, // Pass user ID
    navController: NavController, // NavController for navigation
    onNavigateBack: () -> Unit // Callback for back navigation
) {
    val userAllergies by userAllergiesViewModel.userAllergies.collectAsState()

    // Local copy of allergies (to avoid modifying backend until "Save" is pressed)
    var localAllergies = remember { mutableStateListOf<String>().apply {
        addAll(userAllergies?.allergies ?: emptyList())
    }}

    var isModified by remember { mutableStateOf(false) }

    var showDiscardDialog by remember { mutableStateOf(false) } // State for discard confirmation dialog

    // Observe navigation result
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    //val selectedIngredient = navBackStackEntry?.savedStateHandle?.get<String>("selectedIngredient")
    val selectedIngredientPair = navBackStackEntry?.savedStateHandle?.get<Pair<String, String>>("selectedIngredient")

    LaunchedEffect(userId) {
        userAllergiesViewModel.fetchUserAllergies(userId)
    }

    // Update localAllergies when userAllergies changes
    LaunchedEffect(userAllergies) {
        userAllergies?.let { allergies ->
            localAllergies.clear()
            localAllergies.addAll(allergies.allergies)
        }
    }

    // Add a new allergy when an ingredient is selected
    LaunchedEffect(selectedIngredientPair) {
        selectedIngredientPair?.let { (name, _) ->
            if (!localAllergies.contains(name)) {
                localAllergies.add(name)
                isModified = true
            }
        }
    }

    println("localAllergies: $localAllergies")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ScreenHeader(
            title = "Allergies",
            onNavigateBack = {
                if (isModified) {
                    showDiscardDialog = true
                } else {
                    onNavigateBack()
                }
            },
            rightContent = {
                IconButton(onClick = {
                    navController.navigate("ingredientSearch")
                }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Ingredient")
                }
            }
        )

        // Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Your Allergies",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = fontMedium
                )
            )
        }


        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            localAllergies.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = brightGreen, // Green for active allergies
                            shape = MaterialTheme.shapes.extraSmall
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                    DeleteButton(
                        onClick = {
                            localAllergies.remove(item)
                            isModified = true
                        },
                        contentDescription = "Delete Allergy"
                    )
                }
            }
        }


        // Save Button
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            SaveButton(
                onClick = {
                    userAllergiesViewModel.updateUserAllergies(
                        UserAllergies(userId, localAllergies.toList())
                    )
                    isModified = false
                },
                isEnabled = isModified
            )
        }
    }
    // Show discard dialog if there are unsaved changes
    if (showDiscardDialog) {
        UnsavedChangesDialog(
            onDismiss = { showDiscardDialog = false },
            onConfirmLeave = {
                showDiscardDialog = false
                onNavigateBack()
            }
        )
    }
}
