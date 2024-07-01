import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentManager.TAG
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.milkymate.Nav.NavItem
import com.example.milkymate.R
import com.example.milkymate.Store.presentation.UI.Viewmodels.HomeScreenViewModel
import com.example.milkymate.Store.model.User
import com.example.milkymate.Store.presentation.UI.NavigationScreens
import com.example.milkymate.ui.theme.MilkyMateTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.math.log

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RestrictedApi")
@Composable
fun HomeScreen(navController: NavController, user: User,
               viewModel: HomeScreenViewModel = viewModel() ) {
    try {
        // Navigation code or HomeScreen initialization
        Log.e("MilkyMateHome", "Successfully navigated to HomeScreen")
    } catch (e: Exception) {
        Log.e("MilkyMateError", "Error navigating to HomeScreen", e)
    }
   LaunchedEffect(user) {
      viewModel.setUser(user)
   }



    MilkyMateTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            NavigationScreens(navController = navController as NavHostController ,user = user)



            Scaffold(
                topBar = {
                    TopAppBarContent(viewModel = viewModel)
                },
                content = {
                    HomeScreenContent(navController = navController, viewModel = viewModel)
                },
                bottomBar = {
                    BottomAppBar { BottomNavigationBar(navController = navController) }
                },

            )


        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navItems = listOf(NavItem.Home, NavItem.Products, NavItem.History, NavItem.Profile)
    var selectedItem by rememberSaveable { mutableStateOf(0) }

    NavigationBar {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                alwaysShowLabel = true,
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item.path) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}


@Composable
fun TopAppBarContent(viewModel: HomeScreenViewModel) {
    val user by viewModel.user.collectAsState()
    user?.let { user ->
       Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Welcome, ${user.displayName}!", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Email: ${user.email}", fontSize = 16.sp)
       }
    }
}

@Composable
fun HomeScreenContent(navController: NavController, viewModel: HomeScreenViewModel) {
    val user by viewModel.user.collectAsState()

    user?.let { currentUser ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberImagePainter(
                    data = currentUser.photoUrl ?: "",
                    builder = {
                        crossfade(true)
                        transformations(CircleCropTransformation())
                        error(R.drawable.ic_launcher_background) // Add a default image
                    }
                ),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    Firebase.auth.signOut()
                    navController.navigate("LoginScreen") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            ) {
                Text("Sign Out", fontSize = 16.sp)
            }
        }
    }
}
