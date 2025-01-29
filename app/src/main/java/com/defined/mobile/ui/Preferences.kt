package com.defined.mobile.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Preferences(viewModel: PreferencesViewModel, onNavigateBack: () -> Unit) {
    // Original states from the ViewModel (initial values)
    val originalDairyFree by viewModel.isDairyFree
    val originalGlutenFree by viewModel.isGlutenFree
    val originalPescetarian by viewModel.isPescetarian
    val originalVegan by viewModel.isVegan
    val originalVegetarian by viewModel.isVegetarian

    // States for user interaction (local changes)
    var isDairyFree by remember { mutableStateOf(originalDairyFree) }
    var isGlutenFree by remember { mutableStateOf(originalGlutenFree) }
    var isPescetarian by remember { mutableStateOf(originalPescetarian) }
    var isVegan by remember { mutableStateOf(originalVegan) }
    var isVegetarian by remember { mutableStateOf(originalVegetarian) }

    // Flag to detect if there are any changes
    var hasChanges by remember {
        mutableStateOf(false)
    }

    // Calculate changes dynamically based on user interaction
    val detectChanges: () -> Unit = {
        hasChanges = isDairyFree != originalDairyFree ||
                isGlutenFree != originalGlutenFree ||
                isPescetarian != originalPescetarian ||
                isVegan != originalVegan ||
                isVegetarian != originalVegetarian
    }

    // Trigger detectChanges when any of the checkboxes change
    LaunchedEffect(isDairyFree, isGlutenFree, isPescetarian, isVegan, isVegetarian) {
        detectChanges()
    }

    var showDiscardDialog by remember { mutableStateOf(false) } // State for discard confirmation dialog

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Preferences") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (hasChanges) {
                            showDiscardDialog = true // Show dialog if there are unsaved changes
                        } else {
                            onNavigateBack() // Navigate back without confirmation
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            Button(
                onClick = {
                    // Save preferences and reset states after saving
                    viewModel.savePreferences(
                        dairyFree = isDairyFree,
                        glutenFree = isGlutenFree,
                        pescetarian = isPescetarian,
                        vegan = isVegan,
                        vegetarian = isVegetarian
                    )
                    // Reset hasChanges to false after saving
                    hasChanges = false
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (hasChanges) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant
                ),
                enabled = hasChanges // Only enable the button if there are changes
            ) {
                Text(text = "Save", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Select your dietary preferences:",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            DietPreferenceCheckbox("Dairy Free", isDairyFree) { isDairyFree = it }
            DietPreferenceCheckbox("Gluten Free", isGlutenFree) { isGlutenFree = it }
            DietPreferenceCheckbox("Pescetarian", isPescetarian) { isPescetarian = it }
            DietPreferenceCheckbox("Vegan", isVegan) { isVegan = it }
            DietPreferenceCheckbox("Vegetarian", isVegetarian) { isVegetarian = it }
        }
    }

    // Show discard dialog if there are unsaved changes
    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            title = { Text("Unsaved Changes") },
            text = { Text("You have unsaved changes. Are you sure you want to leave without saving?") },
            confirmButton = {
                TextButton(onClick = {
                    showDiscardDialog = false

                    // Reset temporary states to the original values from the ViewModel
                    isDairyFree = originalDairyFree
                    isGlutenFree = originalGlutenFree
                    isPescetarian = originalPescetarian
                    isVegan = originalVegan
                    isVegetarian = originalVegetarian

                    onNavigateBack()
                }) {
                    Text("Leave")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiscardDialog = false }) {
                    Text("Stay")
                }
            }
        )
    }
}

@Composable
fun DietPreferenceCheckbox(label: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isChecked) Color(0xFF4CAF50) // Bright Green when checked
        else Color(0xFFE0E0E0), // Light Gray when unchecked
        animationSpec = tween(durationMillis = 300) // Smooth transition
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) } // Toggle the state
            .background(
                color = backgroundColor,
                shape = MaterialTheme.shapes.medium
            )
            .padding(12.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            color = if (isChecked) Color.White else Color.Black // Change text color for contrast
        )
        Checkbox(
            checked = isChecked,
            onCheckedChange = null, // Handled by Row's click
            colors = CheckboxDefaults.colors(
                checkedColor = Color.White,
                checkmarkColor = if (isChecked) Color.Black else Color.Transparent // Visible checkmark
            )
        )
    }
}
