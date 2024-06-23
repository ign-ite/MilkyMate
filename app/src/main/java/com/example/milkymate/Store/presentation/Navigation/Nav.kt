package com.example.milkymate.Store.presentation.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.milkymate.Store.presentation.UI.HomeScreen
import com.example.milkymate.Store.presentation.UI.LoginScreen
import com.example.milkymate.Store.model.User
import com.example.milkymate.Store.presentation.UI.Viewmodels.AuthViewModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URLDecoder

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "LoginScreen") {
        composable("LoginScreen") {
            LoginScreen(authViewModel = AuthViewModel())
        }
        composable(
            route = "HomeScreen/{user}",
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
    }
}