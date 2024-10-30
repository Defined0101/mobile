package com.defined.mobile.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainPage(
                onSearchClick = {
                    navController.navigate("search")
                }
            )
        }
        composable("search") {
            SearchScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
