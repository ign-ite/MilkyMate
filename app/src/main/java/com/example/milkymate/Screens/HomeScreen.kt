package com.example.milkymate.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
 import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.milkymate.Data.User
import com.example.milkymate.ui.theme.MilkyMateTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase






@Composable
fun HomeScreen(navController: NavController, user: User) {
    MilkyMateTheme {
        var selectedItemIndex by rememberSaveable {
            mutableStateOf(0)
        }

        val screens = listOf(
            BottomBarScreen.Home,
            BottomBarScreen.Products,
            BottomBarScreen.Orders,
            BottomBarScreen.Profile
        )

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination?.route
        val navController = rememberNavController()
        Scaffold(
            topBar = { TopAppBar() },
            bottomBar = {
                NavigationBar {
                    screens.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = currentDestination == item.route,
                            onClick = {
                                selectedItemIndex = index
                                when (index) {
                                    0 -> navController.navigate("HomeScreen")
                                    1 -> navController.navigate("ProductsScreen")
                                    2 -> navController.navigate("OrdersScreen")
                                    3 -> navController.navigate("ProfileScreen")
                                }
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            label = { Text(item.title) },
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if (item.badgeCount != null) {
                                            // Show badge with count
                                            Text(item.badgeCount.toString())
                                        } else if (item.hasNews) {
                                            // Show empty badge for news
                                            Text(" ")
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (currentDestination == item.route) {
                                            item.selectedIcon
                                        } else {
                                            item.unselectedIcon
                                        },
                                        contentDescription = item.title
                                    )
                                }
                            }
                        )
                    }
                }
            }

        ) { innerPadding ->
            HomeScreenContent(
                navController = navController,
                user = user,
                modifier = Modifier.padding(innerPadding)
            )
            BottomNavGraph(navController = navController, user =user)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar() {
    // Define your top app bar here
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("MilkyMate", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun HomeScreenContent(navController: NavController, user: User, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // here all these texts need to go to the top app bar
        Text(text = "Welcome, ${user.displayName}!", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Email: ${user.email}", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(16.dp))
        user.photoUrl?.let { url ->
            Image(
                painter = rememberImagePainter(url),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            Firebase.auth.signOut()
            navController.navigate("LoginScreen") {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }) {
            Text(text = "Sign Out", fontSize = 16.sp)
        }
    }
}
