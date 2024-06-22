package com.example.milkymate.Screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.milkymate.Data.User
import com.example.milkymate.OrdersScreen
import com.example.milkymate.ProductsScreen
import com.example.milkymate.ProfileScreen

@Composable
fun BottomNavGraph(navController: NavHostController, user: User) {
    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(navController = navController, user = user)
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen(navController=navController)
        }
        composable(route = BottomBarScreen.Orders.route) {
            OrdersScreen(navController=navController)
        }
        composable(route = BottomBarScreen.Products.route) {
            ProductsScreen(navController=navController)
        }
    }
}
