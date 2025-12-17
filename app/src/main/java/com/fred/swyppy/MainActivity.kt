package com.fred.swyppy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.fred.swyppy.navigation.AppNavHost
import com.fred.swyppy.ui.theme.SwyppyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SwyppyTheme() {
                val navController = rememberNavController()
                AppNavHost(navController)
            }
        }
    }
}
