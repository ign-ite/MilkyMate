package com.example.milkymate.Store.presentation.UI.Viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.milkymate.Store.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder



class AuthViewModel : ViewModel() {


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    private val _isError = MutableLiveData<String>()
    val isError: LiveData<String> get() = _isError


    private lateinit var navController: NavController

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    fun loginWithGoogle(idToken: String) {
        val auth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        _isLoading.value = true
        auth.signInWithCredential(credential).addOnCompleteListener { authResult ->
            _isLoading.value = false
            if (authResult.isSuccessful) {
                val firebaseUser = auth.currentUser
                val user = firebaseUser?.uid?.let {
                    User(
                        displayName = firebaseUser?.displayName,
                        photoUrl = firebaseUser?.photoUrl?.toString(),
                        email = firebaseUser?.email,
                        uid = it
                    )
                }
                val userJson = URLEncoder.encode(Json.encodeToString(user), "UTF-8")
                // Navigate to home screen or handle success state here
                _isSuccess.value = true
                navigationToHomeScreen(navController, user.toString())//
            } else {
                _isError.value = "Authentication failed."
            }
        }
    }

    private fun navigationToHomeScreen(navController: NavController,user:String){
        GlobalScope.launch { Dispatchers.Main }

        navController.navigate("HomeScreen/$user")//
    }


}
