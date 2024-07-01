import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.fragment.app.FragmentManager.TAG
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.milkymate.Store.model.User
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
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
     val _shouldNavigateToHome = MutableStateFlow<User?>(null)

   val shouldNavigateToHome: StateFlow<User?> = _shouldNavigateToHome.asStateFlow()

    private val _userData = MutableLiveData<User>()
    val userData: LiveData<User> = _userData

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

    @SuppressLint("RestrictedApi")
    fun loginWithGoogle(idToken: String?) {
        viewModelScope.launch {
            idToken?.let { token ->
                Log.d(TAG, "Attempting Google Sign-In with token: $token")
                val credential = GoogleAuthProvider.getCredential(token, null)
                auth.signInWithCredential(credential).addOnCompleteListener { authResult ->
                    if (authResult.isSuccessful) {
                        Log.d(TAG, "Google Sign-In successful")
                        _authState.value = AuthState.Authenticated
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
                            Log.d(TAG, " Google Sign-In with user: ${user.displayName}")
                        }

                        if (user != null) {
                            _shouldNavigateToHome.value = user
                        }
                    } else {
                        Log.e(TAG, "Google Sign-In failed: ${authResult.exception?.message}")
                        _authState.value = AuthState.Unauthenticated
                    }
                }
            } ?: run {
                Log.e(TAG, "Google ID token is null")
                // Handle case where idToken is null (if required)
            }
        }
    }



    fun com.google.firebase.auth.FirebaseUser.toUser(): User? {
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
        val deepLinkUri = Uri.parse("android-app://androidx.navigation/HomeScreen/$encodedUserJson")
        navController.navigate("HomeScreen/${URLEncoder.encode(Json.encodeToString(user), "UTF-8")}")
    }


}

enum class AuthState {
    Authenticated,
    Unauthenticated
}