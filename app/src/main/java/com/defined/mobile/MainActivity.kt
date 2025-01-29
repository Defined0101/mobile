package com.defined.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import com.defined.mobile.ui.ScreenWithBottomNav
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
