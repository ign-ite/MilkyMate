import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.milkymate.Store.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder

class AuthViewModel : ViewModel() {
    private lateinit var navController: NavController
    private val _shouldNavigateToHome = MutableStateFlow<User?>(null)
    val shouldNavigateToHome: StateFlow<User?> = _shouldNavigateToHome.asStateFlow()

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    fun loginWithGoogle(idToken: String) {
        val auth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {
                val firebaseUser = auth.currentUser
                val user = firebaseUser?.let {
                    User(
                        displayName = it.displayName ?: "",
                        photoUrl = it.photoUrl?.toString() ?: "",
                        email = it.email ?: "",
                        uid = it.uid
                    )
                }
                if (user != null) {
                    _shouldNavigateToHome.value = user
                }
            } else {
                // Handle error
            }
        }
    }

    fun navigateToHomeScreen(user: User) {
        val userJson = URLEncoder.encode(Json.encodeToString(user), "UTF-8")
        navController.navigate("HomeScreen/$userJson")
    }
}
