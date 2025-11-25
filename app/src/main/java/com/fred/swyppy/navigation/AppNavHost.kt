package com.fred.swyppy.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fred.swyppy.ui.screens.about.AboutScreen
import com.fred.swyppy.ui.screens.profile.ProfileScreen
import com.fred.swyppy.ui.screens.home.HomeScreen
import com.fred.swyppy.ui.screens.service.ServiceScreen
import com.fred.swyppy.ui.screens.splash.SplashScreen
import com.fred.swyppy.ui.screens.start.StartScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_SPLASH
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ROUT_HOME) {
            HomeScreen(navController)
        }
        composable(ROUT_ABOUT) {
            AboutScreen(navController)
        }
        composable(ROUT_PROFILE) {
            ProfileScreen(navController)
        }
        composable(ROUT_SERVICE) {
            ServiceScreen(navController)
        }
        composable(ROUT_START) {
            StartScreen(navController)
        }
        composable(ROUT_SPLASH) {
            SplashScreen(navController)
        }


    }
}