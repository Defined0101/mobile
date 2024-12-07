package com.defined.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.defined.mobile.ui.LoginPage
import com.defined.mobile.ui.LoginViewModel
import com.defined.mobile.ui.MainScreen
import com.defined.mobile.ui.theme.MobileTheme
import com.google.firebase.auth.FirebaseAuth

//TODO: Everytime the app restarts, users needs to login. Another system should be implemented.

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Force logout on app start
        FirebaseAuth.getInstance().signOut()

        setContent {
            MobileTheme(darkTheme = isSystemInDarkTheme()) {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}


@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login" // Always start with login
    ) {
        composable("login") {
            val viewModel: LoginViewModel = viewModel()
            LoginPage(
                viewModel = viewModel,
                onSignInClick = { user ->
                    if (user != null) {
                        navController.navigate("main") {
                            popUpTo("login") { inclusive = true } // Remove login page from back stack
                        }
                    }
                }
            )
        }
        composable("main") {
            MainScreen()
        }
    }
}


