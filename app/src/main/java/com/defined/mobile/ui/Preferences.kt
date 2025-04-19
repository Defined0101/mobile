package com.defined.mobile.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.defined.mobile.ui.theme.*

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
        containerColor = TransparentColor,
        topBar = {
            TopAppBar(
                title = { Text("Preferences", color = MaterialTheme.colorScheme.onSecondary)},
                navigationIcon = {
                    BackButton(onNavigateBack = {
                        if (hasChanges) {
                            showDiscardDialog = true // Show dialog if there are unsaved changes
                        } else {
                            onNavigateBack() // Navigate back without confirmation
                        }
                    })
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TransparentColor, // Remove default background
                    titleContentColor = MaterialTheme.colorScheme.onPrimary, // Adjust text color
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary // Adjust icon color
                )
            )
        },
        floatingActionButton = {
            SaveButton(
                onClick = {
                    viewModel.savePreferences(
                        dairyFree = isDairyFree,
                        glutenFree = isGlutenFree,
                        pescetarian = isPescetarian,
                        vegan = isVegan,
                        vegetarian = isVegetarian
                    )
                    hasChanges = false
                },
                isEnabled = hasChanges
            )
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
        UnsavedChangesDialog(
            onDismiss = { showDiscardDialog = false },
            onConfirmLeave = {
                showDiscardDialog = false

                // Reset temporary states to the original values from the ViewModel
                isDairyFree = originalDairyFree
                isGlutenFree = originalGlutenFree
                isPescetarian = originalPescetarian
                isVegan = originalVegan
                isVegetarian = originalVegetarian

                onNavigateBack()
            }
        )
    }
}

@Composable
fun DietPreferenceCheckbox(label: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isChecked) brightGreen // Bright Green when checked
        else lightGray, // Light Gray when unchecked
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
            color = if (isChecked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface // Change text color for contrast
        )
        Checkbox(
            checked = isChecked,
            onCheckedChange = null, // Handled by Row's click
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.background,
                checkmarkColor = if (isChecked) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.surface.copy(alpha = 0f) // Visible checkmark
            )
        )
    }
}
