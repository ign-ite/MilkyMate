package com.example.milkymate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.milkymate.Data.User
import com.example.milkymate.Screens.HomeScreen
import com.example.milkymate.Screens.LoginScreen
import com.example.milkymate.ui.theme.MilkyMateTheme
import kotlinx.serialization.json.Json
import java.net.URLDecoder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MilkyMateTheme {


                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "LoginScreen") {
                    composable("LoginScreen") {
                        LoginScreen(navController = navController)
                    }
                    composable(
                        "HomeScreen/{user}",
                        arguments = listOf(navArgument("user") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val userJson = backStackEntry.arguments?.getString("user")
                        val user = userJson?.let {
                            Json.decodeFromString<User>(URLDecoder.decode(it, "UTF-8"))
                        }
                        if (user != null) {
                            HomeScreen(navController = navController, user = user)
                        } else {
                            navController.navigate("LoginScreen") {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                    composable("ProductsScreen") {
                        ProductsScreen(navController = navController)
                    }
                    composable("OrdersScreen") {
                        OrdersScreen(navController = navController)
                    }
                    composable("ProfileScreen") {
                        ProfileScreen(navController = navController)
                    }

                }

            }
            window.decorView.systemUiVisibility = (
                    android.view.View.SYSTEM_UI_FLAG_IMMERSIVE
                            or android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
                            or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    )
        }
    }
}

