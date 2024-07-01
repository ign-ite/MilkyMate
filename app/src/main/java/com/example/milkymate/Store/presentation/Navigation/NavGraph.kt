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
import com.example.milkymate.Store.presentation.UI.SplashScreen
import com.example.milkymate.Store.presentation.UI.Viewmodels.HomeScreenViewModel
import kotlinx.serialization.encodeToString
import java.net.URLEncoder

@SuppressLint("SuspiciousIndentation")
@Composable
fun NavGraph(navController: NavHostController, authViewModel: AuthViewModel) {
    NavHost(navController = navController, startDestination = "SplashScreen") {
        composable("SplashScreen") {
            SplashScreen(navController=navController)
        }
        composable("LoginScreen") {
            LoginScreen(authViewModel = authViewModel, navController = navController)
        }
        composable("HomeScreen/{user}",

            arguments = listOf(navArgument("user") { type = NavType.StringType }),
            deepLinks = listOf(navDeepLink {
                uriPattern = "android-app://androidx.navigation/HomeScreen/{user}"
            }),

        ) {  backStackEntry ->
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
                navController.navigate("HomeScreen/${URLEncoder.encode(Json.encodeToString(user), "UTF-8")}")

              //  HomeScreen(navController = navController, user = user, viewModel = viewModel())
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