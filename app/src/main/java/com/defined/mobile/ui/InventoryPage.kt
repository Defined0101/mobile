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
import com.defined.mobile.backend.UserIngredientsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.defined.mobile.entities.Ingredient
import com.defined.mobile.entities.UserIngredients

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryPage(
    userIngredientsViewModel: UserIngredientsViewModel,
    userId: String,
    navController: NavController,
    onNavigateBack: () -> Unit
) {
    val userIngredients by userIngredientsViewModel.userIngredients.collectAsState()

    var localInventory = remember { mutableStateListOf<Ingredient>().apply {
        addAll(userIngredients?.ingredients ?: emptyList())
    }}

    var isModified by remember { mutableStateOf(false) }
    var showDiscardDialog by remember { mutableStateOf(false) }

    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    //val selectedIngredientName = navBackStackEntry?.savedStateHandle?.get<String>("selectedIngredient")
    val selectedIngredient = navBackStackEntry?.savedStateHandle?.get<Pair<String, String>>("selectedIngredient")

    val unitCategories = mapOf(
        "cup" to listOf("tea cup", "water glass", "ajda tea glass", "cups"),
        "teaspoon" to listOf("tsp", "teaspoons", "pinch", "tablespoon", "tablespoons", "wipe tablespoon", "tbsp"),
        "piece" to listOf("pieces", "stick", "clove", "cloves", "strip", "strips", "square", "sheet",
            "sheets", "fillet", "fillets", "slice", "slices",
            "package","packages", "boxes", "envelope", "envelopes", "jar",
            "inch", "inches"),
        "dash" to listOf("dashes"),
        "bunch" to listOf("bunches", "bundle", "bond", "stalk", "stalks", "sprig", "sprigs"),
        "liter" to listOf("liters", "ml", "gallon", "quart", "quarts", "shot",
            "bag","bags", "can", "cans", "bottle", "bottles"),
        "gram" to listOf("g", "kg", "kilogram", "grams", "pound", "pounds", "lbs", "lb", "oz", "ounce", "ounces", "bushel"),
        "pint" to listOf("pints"),
        "head" to listOf("heads"),
        "drop" to listOf("drops"),
        "scoop" to listOf("scoop"),
        "leaf" to listOf("leaf")
    )


    LaunchedEffect(userId) {
        if (userId.isNotEmpty() && localInventory.isEmpty()) {
            userIngredientsViewModel.fetchUserIngredients(userId)
        }
    }

    LaunchedEffect(userIngredients) {
        userIngredients?.let { inventory ->
            localInventory.clear()
            localInventory.addAll(inventory.ingredients)
        }
    }

    LaunchedEffect(selectedIngredient) {
        selectedIngredient?.let { (name, defaultUnit) ->
            println("name: $name, default_unit: $defaultUnit")

            if (!localInventory.any { it.name == name }) {
                localInventory.add(Ingredient(name, defaultUnit, defaultUnit, 1f))
                isModified = true
            }
        }
    }

    println("localInventory: " + localInventory)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ScreenHeader(
            title = "Inventory",
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Your Inventory",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = fontMedium
                )
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            localInventory.forEachIndexed { index, item ->

                var expanded by remember { mutableStateOf(false) }
                var selectedUnit by remember { mutableStateOf(item.unit) }
                val defaultUnit by remember { mutableStateOf(item.default_unit) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = brightGreen,
                            shape = MaterialTheme.shapes.extraSmall
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.name,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    )
                    // Quantity TextField
                    TextField(
                        value = item.quantity.toString(),
                        onValueChange = { newValue ->
                            newValue.toFloatOrNull()?.let {
                                localInventory[index] = item.copy(quantity = it, unit = selectedUnit, default_unit = defaultUnit)  // Copy the updated unit and defaultUnit
                                isModified = true
                            }
                        },
                        modifier = Modifier
                            .width(80.dp)
                            .background(TransparentColor), // Fix black background
                        label = { Text("Quantity") },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = TransparentColor,
                            focusedIndicatorColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    // Unit Dropdown Menu
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        TextField(
                            value = selectedUnit,
                            onValueChange = { selectedUnit = it },
                            label = { Text("Unit") },
                            readOnly = true,
                            modifier = Modifier
                                .width(100.dp)
                                .background(TransparentColor)
                                .menuAnchor(),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = TransparentColor,
                                focusedIndicatorColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                unfocusedIndicatorColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .heightIn(max = 330.dp) // Limit the height of the dropdown to 330dp
                        ) {
                            // Get unit category and alternatives
                            val relatedUnits = unitCategories[defaultUnit] ?: emptyList()
                            val allUnits = listOf(defaultUnit) + relatedUnits

                            allUnits.forEach { unit ->
                                DropdownMenuItem(
                                    text = { Text(text = unit) },
                                    onClick = {
                                        selectedUnit = unit
                                        expanded = false
                                        localInventory[index] = item.copy(unit = unit, default_unit = defaultUnit)
                                        isModified = true
                                    }
                                )
                            }
                        }
                    }

                    //Spacer(modifier = Modifier.width(8.dp))
                    DeleteButton(
                        onClick = {
                            localInventory.removeAt(index)
                            isModified = true
                        },
                        contentDescription = "Delete Ingredient"
                    )
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            SaveButton(
                onClick = {
                    userIngredientsViewModel.updateUserIngredients(
                        UserIngredients(userId, localInventory.toList())
                    )
                    isModified = false
                },
                isEnabled = isModified
            )
        }
    }

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
