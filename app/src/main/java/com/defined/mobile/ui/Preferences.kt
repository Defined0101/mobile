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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.defined.mobile.ui.theme.*
import com.defined.mobile.backend.UserPreferencesViewModel
import com.defined.mobile.entities.UserPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Preferences(
    UserPreferencesViewModel: UserPreferencesViewModel = viewModel(),
    userId: String,
    onNavigateBack: () -> Unit
) {
    val userPreferences by UserPreferencesViewModel.userPreferences.collectAsState()

    var selectedPreferences by remember { mutableStateOf<Set<String>>(emptySet()) }
    // Flag to detect if there are any changes
    var hasChanges by remember { mutableStateOf(false) }
    var showDiscardDialog by remember { mutableStateOf(false) } // State for discard confirmation dialog

    val preferenceLabels = listOf("Dairy Free", "Gluten Free", "Pescetarian", "Vegan", "Vegetarian")


    // Detect changes dynamically
    LaunchedEffect(userId) {
        UserPreferencesViewModel.fetchUserPreferences(userId)
    }

    // Set selectedPreferences whenever userPreferences are updated
    LaunchedEffect(userPreferences) {
        userPreferences?.preferences?.let { preferences ->
            selectedPreferences = mutableSetOf<String>().apply {
                if (preferences.dairyFree) add("Dairy Free")
                if (preferences.glutenFree) add("Gluten Free")
                if (preferences.pescetarian) add("Pescetarian")
                if (preferences.vegan) add("Vegan")
                if (preferences.vegetarian) add("Vegetarian")
            }
        }
        hasChanges = false // Reset `hasChanges` after data is fetched
    }


    // Detect changes when the selection changes
    val detectChanges: () -> Unit = {
        hasChanges = selectedPreferences != userPreferences?.preferences?.let { preferences ->
            mutableSetOf<String>().apply {
                if (preferences.dairyFree) add("Dairy Free")
                if (preferences.glutenFree) add("Gluten Free")
                if (preferences.pescetarian) add("Pescetarian")
                if (preferences.vegan) add("Vegan")
                if (preferences.vegetarian) add("Vegetarian")
            }
        }
    }

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
                    // Create a UserPreferences object to save
                    val updatedPreferences = UserPreferences(
                        userId = userId,
                        preferences = com.defined.mobile.entities.Preferences(
                            dairyFree = "Dairy Free" in selectedPreferences,
                            glutenFree = "Gluten Free" in selectedPreferences,
                            pescetarian = "Pescetarian" in selectedPreferences,
                            vegan = "Vegan" in selectedPreferences,
                            vegetarian = "Vegetarian" in selectedPreferences
                        )
                    )
                    // Save the updated preferences
                    UserPreferencesViewModel.updateUserPreferences(updatedPreferences)
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

            val preferenceLabels = listOf("Dairy Free", "Gluten Free", "Pescetarian", "Vegan", "Vegetarian")

            preferenceLabels.forEach { label ->
                DietPreferenceCheckbox(
                    label = label,
                    isChecked = label in selectedPreferences,
                    onCheckedChange = {
                        selectedPreferences = if (it) selectedPreferences + label else selectedPreferences - label
                        detectChanges()
                    }
                )
            }
        }
    }

    // Show discard dialog if there are unsaved changes
    if (showDiscardDialog) {
        UnsavedChangesDialog(
            onDismiss = { showDiscardDialog = false },
            onConfirmLeave = {
                // Restore the original preferences from the fetched data
                userPreferences?.preferences?.let { preferences ->
                    selectedPreferences = mutableSetOf<String>().apply {
                        if (preferences.dairyFree) add("Dairy Free")
                        if (preferences.glutenFree) add("Gluten Free")
                        if (preferences.pescetarian) add("Pescetarian")
                        if (preferences.vegan) add("Vegan")
                        if (preferences.vegetarian) add("Vegetarian")
                    }
                }
                hasChanges = false
                showDiscardDialog = false
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
