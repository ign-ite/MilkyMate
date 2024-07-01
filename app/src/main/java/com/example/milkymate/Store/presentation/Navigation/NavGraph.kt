import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.milkymate.Store.model.User
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import android.util.Log
import androidx.navigation.NavType
import com.example.milkymate.Store.presentation.UI.LoginScreen

@SuppressLint("SuspiciousIndentation")
@Composable
fun NavGraph(navController: NavHostController, authViewModel: AuthViewModel) {
    NavHost(navController = navController, startDestination = "LoginScreen") {
        composable("LoginScreen") {
            LoginScreen(authViewModel = authViewModel , navController =navController)
        }
        composable(
            route = "home/{displayName}/{email}/{photoUrl}/{uid}",
            arguments = listOf(navArgument("displayName") { type = NavType.StringType },
                navArgument("uid") { type = NavType.StringType },
                        navArgument("email") { type = NavType.StringType },
                        navArgument("photoUrl") { type = NavType.StringType }),
            deepLinks = listOf(navDeepLink {
                uriPattern = "android-app://androidx.navigation/home/{user}"
            })
        ) { backStackEntry ->
            val userJson = backStackEntry.arguments?.getString("user")
            val user = userJson?.let {
                try {
                    Json.decodeFromString<User>(URLDecoder.decode(it, "UTF-8"))
                } catch (e: Exception) {
                    Log.e("Navigation", "Failed to decode user JSON", e)
                    null
                }
            }
            if (user != null) {
                HomeScreen(navController = navController, user = user, viewModel = viewModel())
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