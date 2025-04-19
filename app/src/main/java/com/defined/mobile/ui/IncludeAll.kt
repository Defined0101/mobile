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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun ScreenWithBottomNav() {
    val items = getBottomNavItems()
    val navController = rememberNavController()

    val routesWithoutBottomNav = setOf("login")

    // Map routes to bottom nav index
    val routeToIndex = mapOf(
        "main" to 0,
        "search/{backActive}" to 1,
        "likedRecipes/{backActive}" to 2,
        "profile" to 3,
        "savedRecipes" to 3,
        "profileInformation" to 3,
        "favourites" to 3,
        "allergies" to 3,
        "preferences" to 3
    )

    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }

    // Observe current route and update selectedIndex
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    println("current route $currentRoute")

    // Determine if BottomNavBar should be shown or hidden
    val showBottomNavBar = currentRoute !in routesWithoutBottomNav

    // Update selected index based on current route
    selectedItemIndex = routeToIndex[currentRoute] ?: 0 // Default to Home if route not mapped

    Scaffold(
        bottomBar = {
            if (showBottomNavBar)
                BottomNavBar(
                    items = items,
                    onItemSelected = { index ->
                        selectedItemIndex = index
                        when (index) {
                            0 -> navController.navigate("main") { launchSingleTop = true }
                            1 -> navController.navigate("search/false") { launchSingleTop = true }
                            2 -> navController.navigate("likedRecipes/false") { launchSingleTop = true }
                            3 -> navController.navigate("profile") { launchSingleTop = true }
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

    val viewModel: LoginViewModel = viewModel()

    val currentUser = viewModel.currentUser()

    if (currentUser != null) {
        println("${currentUser.displayName} already logged in.")
        println("${currentUser.email} already logged in.")
    }

    val startDestination = if (currentUser != null) "main" else "login"

    NavHost(navController, startDestination = startDestination) {
        composable("login") {
            LoginPage(
                viewModel = viewModel,
                onSignInClick = { user ->
                    if (user != null) {
                        println(user.uid)
                        println(user.displayName)
                        println(user.email)
                    }
                    if (user != null) {
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
            ProfileScreen(navController = navController, viewModel = viewModel)
        }
        composable("profile") {
            ProfileScreen(navController = navController, viewModel = viewModel)
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
