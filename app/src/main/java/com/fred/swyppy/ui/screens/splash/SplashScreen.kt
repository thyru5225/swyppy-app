package com.fred.swyppy.ui.screens.splash

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fred.swyppy.ui.theme.newpurple
import com.fred.swyppy.R
import com.fred.swyppy.navigation.ROUTE_MAIN_SCREEN
import com.fred.swyppy.navigation.ROUT_LOGIN
import com.fred.swyppy.navigation.ROUT_START
import com.fred.swyppy.utils.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SplashScreen(navController: NavController) {
    val sessionManager = SessionManager(LocalContext.current)
    //Navigation
    val coroutineScope = rememberCoroutineScope()
    coroutineScope.launch {
        delay(2000)
        // Check if user is logged in
        if (sessionManager.isLoggedIn()) {
            // User is logged in, go to main screen
            navController.navigate(ROUTE_MAIN_SCREEN) {
                popUpTo(0) { inclusive = true }
            }
        } else {
            // User not logged in, go to login screen
            navController.navigate(ROUT_START) {
                popUpTo(0) { inclusive = true }
            }
        }
    }





    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Image(
            painter = painterResource(id = R.drawable.swyppy),
            contentDescription = "logo",
            modifier = Modifier.size(350.dp, 300.dp),
            contentScale = ContentScale.Crop
        )


    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview(){
    SplashScreen(rememberNavController())


}