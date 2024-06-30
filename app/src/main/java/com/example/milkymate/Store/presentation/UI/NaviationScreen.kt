package com.example.milkymate.Store.presentation.UI

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.milkymate.Nav.NavItem

@Composable
fun NavigationScreens(navController: NavHostController) {
    NavHost(navController, startDestination = NavItem.Home.path) {
        composable(NavItem.Home.path) { HomeScreen() }
        composable(NavItem.Products.path) { ProductsScreen() }
        composable(NavItem.History.path) { HistoryScreen() }
        composable(NavItem.Profile.path) { ProfileScreen() }
    }
}