package com.fred.swyppy.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.fred.swyppy.data.PropertyViewModel
import com.fred.swyppy.ui.screens.admin.AddPropertyScreen
import com.fred.swyppy.ui.screens.admin.UpdatePropertyScreen
import com.fred.swyppy.ui.screens.admin.ViewPropertyScreen
import com.fred.swyppy.ui.screens.auth.LoginScreen
import com.fred.swyppy.ui.screens.auth.RegisterScreen
import com.fred.swyppy.ui.screens.splash.SplashScreen
import com.fred.swyppy.ui.screens.start.StartScreen
import com.fred.swyppy.ui.screens.user.MainScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    propertyViewModel: PropertyViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = ROUT_SPLASH
    ) {
        // Main User Screen
        composable(ROUTE_MAIN_SCREEN) {
            MainScreen(
                navController = navController
            )
        }

        // Add Property Screen
        composable(ROUTE_ADD_PROPERTY) {
            AddPropertyScreen(
                navController = navController
            )
        }

        //View Properties Screen (Admin)
        composable(ROUTE_VIEW_PROPERTY) {
            ViewPropertyScreen(
                navController = navController


            )
        }

        // Update Property Screen
        composable(
            route = "$ROUTE_UPDATE_PROPERTY/{propertyId}",
            arguments = listOf(navArgument("propertyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId") ?: ""
            UpdatePropertyScreen(
                navController = navController,
                propertyId = propertyId
            )
        }

        // Login Screen
        composable(ROUT_LOGIN) {
            LoginScreen(navController = navController)
        }

        // Start Screen
        composable(ROUT_START) {
            StartScreen(navController = navController)
        }

        // Splash Screen
        composable(ROUT_SPLASH) {
            SplashScreen(navController = navController)
        }
        composable(ROUT_REGISTER) {
            RegisterScreen(navController = navController)
        }
    }
}
