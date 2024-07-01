import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var navController: NavController
    private val _shouldNavigateToHome = MutableStateFlow<User?>(null)
    val shouldNavigateToHome: StateFlow<User?> = _shouldNavigateToHome.asStateFlow()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        auth.currentUser?.let { firebaseUser ->
            _user.value = firebaseUser.toUser()
        }
    }

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            // val auth = FirebaseAuth.getInstance()
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    _authState.value= AuthState.Authenticated
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
                    _authState.value=AuthState.Unauthenticated
                }
            }
        }
    }

    private fun com.google.firebase.auth.FirebaseUser.toUser(): User? {
        return email?.let {
            displayName?.let { it1 ->
                photoUrl?.toString()?.let { it2 ->
                    User(
                        uid = uid,
                        email = it,
                        displayName = it1,
                        photoUrl = it2
                    )
                }
            }
        }
    }

    fun navigateToHomeScreen(navController: NavController, user: User) {
        val userJson = Json.encodeToString(user)
        val encodedUserJson = Uri.encode(userJson)
     //   val deepLinkUri = Uri.parse("android-app://androidx.navigation/home/$encodedUserJson")
      //  navController.navigate(deepLinkUri)
    }

}

enum class AuthState {
    Authenticated,
    Unauthenticated
}