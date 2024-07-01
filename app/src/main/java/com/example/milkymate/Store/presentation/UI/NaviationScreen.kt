package com.example.milkymate.Store.presentation.UI

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.milkymate.Nav.NavItem
import com.example.milkymate.Store.model.User

@Composable
fun NavigationScreens(navController: NavController,user: User) {
    NavHost(navController = NavHostController(context = LocalContext.current), startDestination = NavItem.Home.path) {
        composable(NavItem.Home.path) { HomeScreen(navController= navController , user = user) }
        composable(NavItem.Products.path) { ProductsScreen() }
        composable(NavItem.History.path) { HistoryScreen() }
        composable(NavItem.Profile.path) { ProfileScreen() }
    }
}