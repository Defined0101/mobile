package com.defined.mobile.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen() {
    // Creates a navigation controller to manage app navigation
    val navController = rememberNavController()

    // Navigation host that holds and manages composable destinations (pages)
    NavHost(
        navController = navController, // Controller used to navigate between destinations
        startDestination = "main" // The initial screen shown when app opens
    ) {
        // Composable for the main page
        composable("main") {
            MainPage(
                onSearchClick = {
                    // Navigates to the search screen when search is clicked
                    navController.navigate("search")
                }
            )
        }
        // Composable for the search screen
        composable("search") {
            SearchScreen(
                onBackClick = {
                    // Returns to the previous screen (main page) when back is clicked
                    navController.popBackStack()
                }
            )
        }
    }
}
