package com.example.milkymate.Screens

import android.media.Image
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberImagePainter
import com.example.milkymate.Data.User
import com.example.milkymate.ui.theme.MilkyMateTheme
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase




@Composable
fun HomeScreen(navController: NavController,
               user: User) {
    MilkyMateTheme {


        Surface(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = { TopAppBar() },
                bottomBar = { BottomNavigationBar(navController= navController) }
            ) { innerPadding ->
                BottomNavGraph(navController = navController)
                HomeScreenContent(
                    navController = navController,
                    user = user,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar() {
    // Define your top app bar here
    CenterAlignedTopAppBar(
        title = { Text("MilkyMate") }
    )
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Orders,
        BottomBarScreen.Profile,
        BottomBarScreen.Products
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

        NavigationBar {
        items.forEachIndexed{index , item->
            NavigationBarItem(
                selected = selectedItemIndex==index,
                onClick = {selectedItemIndex= index
                    when (index) {
                        0 -> navController.navigate("HomeScreen")
                        1 -> navController.navigate("ProductsScreen")
                        2 -> navController.navigate("OrdersScreen")
                        3 -> navController.navigate("ProfileScreen")
                    }
                          },
                label = {
                    Text(item.title)
                },
                alwaysShowLabel = false,
                icon={
                    BadgedBox(
                        badge = {
                            if(item.badgeCount!= null){
                                Badge(content = {Text(item.badgeCount.toString())})
                            }else if( item.hasNews){
                                Badge()
                            }

                        }
                    ) {
                        Icon(
                            imageVector = if(index == selectedItemIndex){
                                item.selectedIcon
                            } else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    }

                }
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController= NavHostController
) {
    BottomNavigationItem()
}



@Composable
fun HomeScreenContent(navController: NavController, user: User, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome, ${user.displayName}!", fontSize = 24.sp)
        Text(text = "Email: ${user.email}", fontSize = 16.sp)
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
        Button(onClick = {
            Firebase.auth.signOut()
            navController.navigate("LoginScreen") {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }) {
            Text("Sign Out", fontSize = 16.sp)
        }
    }
}


