package com.defined.mobile.ui

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
                        0 -> navController.navigate("main")
                        1 -> navController.navigate("search/false")
                        2 -> navController.navigate("likedRecipes/false")
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

    NavHost(navController, startDestination = "login") {
        composable("login") {
            val viewModel: LoginViewModel = viewModel()
            LoginPage(
                viewModel = viewModel,
                onSignInClick = { user ->
                    println(user)
                    if (user != null) {
                        println(user)
                        navController.navigate("main") {
                            popUpTo("login") { inclusive = true } // Remove login page from back stack
                        }
                    }
                }
            )
        }
        composable("main") {
            MainPage(
                navController = navController,
                onSearchClick = { navController.navigate("search/true") }
            )
        }
        composable("search/{backActive}") { backStackEntry ->
            val backActive = backStackEntry.arguments?.getString("backActive")?.toBoolean() ?: false
            SearchScreen(
                navController = navController,
                backActive = backActive,
                onBackClick = { navController.popBackStack() }
            )
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
                onSave = { /* TODO: Implement save logic */ }
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
        composable("savedRecipes") {
            SavedRecipePage(
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("likedRecipes/{backActive}") { backStackEntry ->
            val backActive = backStackEntry.arguments?.getString("backActive")?.toBoolean() ?: false
            LikedRecipePage(
                navController = navController,
                backActive = backActive,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("recipePage/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipe_id") ?: "0"
            RecipePage(
                recipeId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
