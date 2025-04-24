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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import com.defined.mobile.R
import androidx.compose.ui.layout.ContentScale
import com.defined.mobile.backend.DislikedRecipeViewModel
import com.defined.mobile.backend.LikedRecipeViewModel
import com.defined.mobile.backend.PreferencesViewModel
import com.defined.mobile.backend.SavedRecipeViewModel
import com.defined.mobile.backend.UserIngredientsViewModel
import com.defined.mobile.backend.CategoryViewModel
import com.defined.mobile.backend.RecipeViewModel
import com.defined.mobile.backend.ShoppingListViewModel

@Composable
fun ScreenWithBottomNav() {
    val items = getBottomNavItems()
    val navController = rememberNavController()

    val routesWithoutBottomNav = setOf("login")

    // Map routes to bottom nav index
    val routeToIndex = mapOf(
        "main" to 0,
        "search/{backActive}/{selectedCategory}" to 1,
        "likedRecipes/{backActive}" to 2,
        "dislikedRecipes/{backActive}" to 3,
        "profile" to 3,
        "savedRecipes/{backActive}" to 3,
        "profileInformation" to 3,
        "favourites" to 3,
        "allergies" to 3,
        "preferences" to 3,
        "ingredients" to 3,
        "shoppingListPage" to 3
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

    println("selectedItemIndex: " + selectedItemIndex)

    val recipeViewModel: RecipeViewModel = viewModel()
    val categoryViewModel: CategoryViewModel = viewModel()
    val preferencesViewModel: PreferencesViewModel = viewModel()
    val shoppingListViewModel: ShoppingListViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()
    val savedRecipeViewModel: SavedRecipeViewModel = viewModel()
    val likedRecipeViewModel: LikedRecipeViewModel = viewModel()
    val dislikedRecipeViewModel: DislikedRecipeViewModel = viewModel()
    val userIngredientsViewModel: UserIngredientsViewModel = viewModel()

    Scaffold(
        bottomBar = {
            if (showBottomNavBar)
                BottomNavBar(
                    items = items,
                    onItemSelected = { index ->
                        selectedItemIndex = index
                        when (index) {
                            0 -> navController.navigate("main") { launchSingleTop = true }
                            1 -> navController.navigate("search/false/_") { launchSingleTop = true }
                            2 -> navController.navigate("likedRecipes/false") { launchSingleTop = true }
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
                    AppNavigation(
                        navController = navController,
                        recipeViewModel = recipeViewModel,
                        categoryViewModel = categoryViewModel,
                        preferencesViewModel = preferencesViewModel,
                        shoppingListViewModel = shoppingListViewModel,
                        loginViewModel = loginViewModel,
                        savedRecipeViewModel = savedRecipeViewModel,
                        likedRecipeViewModel = likedRecipeViewModel,
                        dislikedRecipeViewModel = dislikedRecipeViewModel,
                        userIngredientsViewModel = userIngredientsViewModel
                    )
                }
            }
        }
    )
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    recipeViewModel: RecipeViewModel,
    categoryViewModel: CategoryViewModel,
    preferencesViewModel: PreferencesViewModel,
    shoppingListViewModel: ShoppingListViewModel,
    loginViewModel: LoginViewModel,
    savedRecipeViewModel: SavedRecipeViewModel,
    likedRecipeViewModel: LikedRecipeViewModel,
    dislikedRecipeViewModel: DislikedRecipeViewModel,
    userIngredientsViewModel: UserIngredientsViewModel
) {

    val currentUser = loginViewModel.currentUser()

    var intId by remember { mutableStateOf<String?>(null) }

    if (currentUser != null) {
        println("${currentUser.displayName} already logged in.")
        println("${currentUser.email} already logged in.")

        loginViewModel.getIntID { id ->
            println("intId: $intId")
            intId = id
        }

        intId?.let { userId ->
            // Now that we have the internal user ID, fetch the recipes
            LaunchedEffect(userId) {
                println("userId: $userId")
                recipeViewModel.fetchRecipes(userId, forceRefresh = true)
            }
        }
    }



    val startDestination = if (currentUser != null) "main" else "login"

    NavHost(navController, startDestination = startDestination) {
        composable("login") {
            LoginPage(
                viewModel = loginViewModel,
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
                recipeViewModel = recipeViewModel,
                categoryViewModel = categoryViewModel,
                shoppingListViewModel = shoppingListViewModel,
                loginViewModel = loginViewModel,
                navController = navController,
                onSearchClick = { navController.navigate("search/true/_") }
            )
        }
        composable("search/{backActive}/{selectedCategory}") { backStackEntry ->
            val backActive = backStackEntry.arguments?.getString("backActive")?.toBoolean() ?: false
            val selectedCategory = backStackEntry.arguments?.getString("selectedCategory").orEmpty()
                .takeIf { it != "_" } ?: "" // "_" geçildiyse bunu gerçek boşluk gibi ele al

            SearchScreen(
                navController = navController,
                backActive = backActive,
                onBackClick = { navController.popBackStack() },
                initialSelectedCategory = selectedCategory,
                categoryViewModel = categoryViewModel,
                preferencesViewModel = preferencesViewModel,
                shoppingListViewModel = shoppingListViewModel,
            )
        }
        composable("favorites") {
            ProfileScreen(navController = navController, viewModel = loginViewModel)
        }
        composable("profile") {
            ProfileScreen(navController = navController, viewModel = loginViewModel)
        }
        composable("profileInformation") {
            ProfileInformation(
                viewModel = loginViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("preferences") {
            var intId by remember { mutableStateOf<String?>(null) }

            if (currentUser == null) {
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                LaunchedEffect(Unit) {
                    loginViewModel.getIntID { id ->
                        intId = id
                    }
                }

                intId?.let { userId ->
                    Preferences(
                        userId = userId,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
        composable("allergies") {
            var intId by remember { mutableStateOf<String?>(null) }

            if (currentUser == null) {
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                // Sadece bir kez çalışsın
                LaunchedEffect(Unit) {
                    loginViewModel.getIntID { id ->
                        intId = id
                    }
                }

                intId?.let { userId ->
                    AllergyPage(
                        navController = navController,
                        userId = userId,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
        composable("ingredients") {
            var userId by remember { mutableStateOf<String?>(null) }

            if (currentUser == null) {
                // Navigate to login if there is no user
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                // Only call this once
                LaunchedEffect(Unit) {
                    loginViewModel.getIntID { id ->
                        userId = id
                    }
                }

                userId?.let { id ->
                    InventoryPage(
                        navController = navController,
                        userId = id, // Pass the userId here
                        onNavigateBack = { navController.popBackStack() },
                        userIngredientsViewModel = userIngredientsViewModel
                    )
                }
            }
        }
        composable("ingredientSearch") {
            IngredientSearch(
                navController = navController,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("savedRecipes/{backActive}") { backStackEntry ->
            var intId by remember { mutableStateOf<String?>(null) }
            val backActive = backStackEntry.arguments?.getString("backActive")?.toBoolean() ?: false

            if (currentUser == null) {
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                LaunchedEffect(Unit) {
                    loginViewModel.getIntID { id ->
                        intId = id
                    }
                }

                intId?.let { userId ->
                    SavedRecipePage(
                        userId = userId,
                        navController = navController,
                        onBackClick = { navController.popBackStack() },
                        savedRecipeViewModel = savedRecipeViewModel,
                        backActive = backActive
                    )
                }
            }
        }
        composable("recipePage/{recipeId}") { backStackEntry ->
            var intId by remember { mutableStateOf<String?>(null) }
            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toIntOrNull() ?: 0

            if (currentUser == null) {
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                LaunchedEffect(Unit) {
                    loginViewModel.getIntID { id ->
                        intId = id
                    }
                }

                println("recipeId: " + recipeId)

                intId?.let { userId ->
                    RecipePage(
                        userId = userId,
                        recipeId = recipeId,
                        onBackClick = { navController.popBackStack() },
                        likedRecipeViewModel = likedRecipeViewModel,
                        savedRecipeViewModel = savedRecipeViewModel,
                        dislikedRecipeViewModel = dislikedRecipeViewModel,
                        shoppingListViewModel = shoppingListViewModel
                    )
                }
            }
        }
        composable("likedRecipes/{backActive}") { backStackEntry ->
            var intId by remember { mutableStateOf<String?>(null) }
            val backActive = backStackEntry.arguments?.getString("backActive")?.toBoolean() ?: false

            if (currentUser == null) {
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                LaunchedEffect(Unit) {
                    loginViewModel.getIntID { id ->
                        intId = id
                    }
                }

                intId?.let { userId ->
                    LikedRecipePage(
                        userId = userId,
                        navController = navController,
                        backActive = backActive,
                        onBackClick = { navController.popBackStack() },
                        likedRecipeViewModel = likedRecipeViewModel
                    )
                }
            }
        }
        composable("dislikedRecipes/{backActive}") { backStackEntry ->
            var intId by remember { mutableStateOf<String?>(null) }
            val backActive = backStackEntry.arguments?.getString("backActive")?.toBoolean() ?: false

            if (currentUser == null) {
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                LaunchedEffect(Unit) {
                    loginViewModel.getIntID { id ->
                        intId = id
                    }
                }
                intId?.let { userId ->
                    DislikedRecipePage(
                        userId = userId,
                        navController = navController,
                        backActive = backActive,
                        onBackClick = { navController.popBackStack() },
                        dislikedRecipeViewModel = dislikedRecipeViewModel
                    )
                }
            }
        }
        composable("shoppingListPage") {
            ShoppingListPage(
                shoppingListViewModel = shoppingListViewModel,
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
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
