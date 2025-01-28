package com.defined.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.defined.mobile.ui.AllergyPage
import com.defined.mobile.ui.IngredientSearch
import com.defined.mobile.ui.theme.MobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MobileTheme(darkTheme = isSystemInDarkTheme()) {
                ScreenWithBottomNav()
            }
        }
    }
}

//@Composable
//fun AppNavigation(navController: NavHostController) {
//    NavHost(
//        navController = navController,
//        startDestination = "login" // Always start with login
//    ) {
//        composable("login") {
//            val viewModel: LoginViewModel = viewModel()
//            LoginPage(
//                viewModel = viewModel,
//                onSignInClick = { user ->
//                    if (user != null) {
//                        navController.navigate("main") {
//                            popUpTo("login") { inclusive = true } // Remove login page from back stack
//                        }
//                    }
//                }
//            )
//        }
//        composable("main") {
//            MainScreen()
//        }
//    }
//}


