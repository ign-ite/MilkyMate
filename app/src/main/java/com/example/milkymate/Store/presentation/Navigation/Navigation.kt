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

@Composable
fun Navigation(navController: NavHostController, authViewModel: AuthViewModel) {
    LaunchedEffect(navController) {
        authViewModel.setNavController(navController)
    }

    val user = authViewModel.shouldNavigateToHome.collectAsState().value
    LaunchedEffect(user) {
        user?.let {
            authViewModel.navigateToHomeScreen(it)
        }
    }

    NavHost(navController = navController, startDestination = "LoginScreen") {
        composable("LoginScreen") {
            LoginScreen(authViewModel = authViewModel)
        }
        composable(
            route = "HomeScreen/{user}",
            arguments = listOf(navArgument("user") { type = NavType.StringType })
        ) { backStackEntry ->
            val userJson = backStackEntry.arguments?.getString("user")
            Log.d("Navigation", "User JSON: $userJson") // Log the JSON string
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
