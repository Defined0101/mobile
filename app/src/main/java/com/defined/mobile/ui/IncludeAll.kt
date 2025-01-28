package com.defined.mobile

import ProfileInformation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.defined.mobile.ui.AllergyPage
import com.defined.mobile.ui.IngredientSearch
import com.defined.mobile.ui.SavedRecipePage

@Composable
fun ScreenWithBottomNav() {
    val items = getBottomNavItems() // Define your bottom navigation items
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavBar(
                items = items,
                onItemSelected = { index ->
                    selectedItemIndex = index
                    when (index) {
                        3 -> navController.navigate("profile") // Navigate to Profile
                    }
                },
                selectedIndex = selectedItemIndex
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                AppNavigation(navController)
            }
        }
    )
}

@Composable
fun AppNavigation(navController: NavHostController) {
    // Shared ViewModel for state management
    val preferencesViewModel: PreferencesViewModel = viewModel()

    NavHost(navController, startDestination = "profile") {
        composable("main") {
            ProfileScreen(navController = navController)
        }
        composable("search") {
            ProfileScreen(navController = navController)
        }
        composable("favorites") {
            ProfileScreen(navController = navController)
        }
        composable("profile") {
            ProfileScreen(navController = navController)
        }
        composable("profileInformation") {
            ProfileInformation(
                onNavigateBack = { navController.popBackStack() },
                onSave = { /* Implement save logic */ }
            )
        }
        composable("preferences") {
            Preferences(
                viewModel = preferencesViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("allergies") {
            AllergyPage(
                navController = navController,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("ingredientSearch") {
            IngredientSearch(
                navController = navController,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("saved_recipes") {
            SavedRecipePage(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
