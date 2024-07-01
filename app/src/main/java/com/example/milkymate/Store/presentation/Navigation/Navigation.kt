import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.milkymate.Store.presentation.UI.LoginScreen
import com.example.milkymate.Store.model.User
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.navDeepLink
import com.example.milkymate.Store.presentation.UI.Viewmodels.HomeScreenViewModel

@SuppressLint("SuspiciousIndentation")
@Composable
fun Navigation(navController: NavHostController, authViewModel: AuthViewModel) {
    LaunchedEffect(navController) {
        authViewModel.setNavController(navController)
    }

    val user = authViewModel.shouldNavigateToHome.collectAsState().value
    LaunchedEffect(user) {
        user?.let {
            try {
                Log.d("Navigation", "Attempting to navigate to HomeScreen with user: ${it.displayName}")
                authViewModel.navigateToHomeScreen(it)
            } catch (e: Exception) {
                Log.e("Navigation", "Navigation failed", e)
                // Handle navigation failure
            }
        }
    }

    NavHost(navController = navController, startDestination = "LoginScreen") {
        composable("LoginScreen") {
            LoginScreen(authViewModel = authViewModel)
        }
        composable(
            route = "HomeScreen/{user}",

            //arguments = listOf(navArgument("user") { type = NavType.StringType }),

        ) { backStackEntry ->
           val userJson = backStackEntry.arguments?.getString("user")
             Log.d("Navigation", "Received user JSON: $userJson")
             val user = userJson?.let {
                 try {
                     Json.decodeFromString<User>(URLDecoder.decode(it, "UTF-8"))
                 } catch (e: Exception) {
                     Log.e("Navigation", "Failed to decode user JSON", e)
                     null
                 }
             }
             if (user != null) {
                 HomeScreen(navController = navController, user = user, viewModel = HomeScreenViewModel())
             } else {
                 LaunchedEffect(Unit) {
                     Log.w("Navigation", "User is null, navigating back to LoginScreen")
                     navController.navigate("LoginScreen") {
                         popUpTo(navController.graph.startDestinationId) {
                             inclusive = true
                         }
                     }
                 }
             }
        }
    }
}
