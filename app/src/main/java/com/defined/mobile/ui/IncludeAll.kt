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
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.defined.mobile.R
import androidx.compose.ui.layout.ContentScale
import com.defined.mobile.backend.DislikedRecipeViewModel
import com.defined.mobile.backend.LikedRecipeViewModel
import com.defined.mobile.backend.PreferencesViewModel
import com.defined.mobile.backend.SavedRecipeViewModel
import com.defined.mobile.backend.UserIngredientsViewModel

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
        "preferences" to 3,
        "ingredients" to 3
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
                            2 -> navController.navigate("likedRecipes/true") { launchSingleTop = true } // changed false to true
                            3 -> navController.navigate("profile") { launchSingleTop = true }
                        }
                    },
                    selectedIndex = selectedItemIndex
                )
        },
        content = { paddingValues ->
            BackgroundContainer{
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    AppNavigation(navController)
                }
            }
        }
    )
}

@Composable
fun AppNavigation(navController: NavHostController) {
    // Shared ViewModel for state management
    val likedRecipeViewModel: LikedRecipeViewModel = viewModel()
    val dislikedRecipeViewModel: DislikedRecipeViewModel = viewModel()
    val savedRecipeViewModel: SavedRecipeViewModel = viewModel()

    val userIngredientsViewModel: UserIngredientsViewModel = viewModel()

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
        composable("preferences") { backStackEntry ->
            currentUser?.let { user ->
                // Pass userId from currentUser to Preferences
                Preferences(userId = user.uid, onNavigateBack = { navController.popBackStack() })
            } ?: run {
                // Handle case when currentUser is null, maybe redirect to login
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true } // Clear the back stack if user isn't logged in
                }
            }
        }
        composable("allergies") {

            currentUser?.let { user ->
                println("user.uid: " + user.uid)
                AllergyPage(
                    navController = navController,
                    userId = user.uid, // Pass the userId
                    onNavigateBack = { navController.popBackStack() }
                )
            } ?: run {
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
        composable("ingredientSearch") {
            IngredientSearch(
                navController = navController,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("savedRecipes") {
            currentUser?.let { user ->
                //println("user.uid: " + user.uid)
                SavedRecipePage(
                    userId = user.uid,
                    navController = navController,
                    onBackClick = { navController.popBackStack() },
                    savedRecipeViewModel = savedRecipeViewModel
                )
            } ?: run {
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
        composable("recipePage/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toIntOrNull() ?: 0

            currentUser?.let { user ->
                //println("user.uid: " + user.uid)
                RecipePage(
                    userId = user.uid,
                    recipeId = recipeId,
                    onBackClick = { navController.popBackStack() },
                    likedRecipeViewModel = likedRecipeViewModel,
                    savedRecipeViewModel = savedRecipeViewModel,
                    dislikedRecipeViewModel = dislikedRecipeViewModel
                )
            } ?: run {
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
        composable("likedRecipes/{backActive}") { backStackEntry ->
            val backActive = backStackEntry.arguments?.getString("backActive")?.toBoolean() ?: false

            currentUser?.let { user ->
                println("user.uid: " + user.uid)
                LikedRecipePage(
                    userId = user.uid,
                    navController = navController,
                    backActive = backActive,
                    onBackClick = { navController.popBackStack() },
                    likedRecipeViewModel = likedRecipeViewModel
                )
            } ?: run {
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
        composable("ingredients") {

            currentUser?.let { user ->
                println("user.uid: " + user.uid)
                InventoryPage(
                    navController = navController,
                    userId = user.uid, // Pass the userId
                    onNavigateBack = { navController.popBackStack() },
                    userIngredientsViewModel = userIngredientsViewModel
                )
            } ?: run {
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
    }
}


@Composable
fun BackgroundContainer(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_image),
            contentDescription = "Background image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        content()
    }
}
