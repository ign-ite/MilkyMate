package com.example.milkymate

import AuthViewModel
import Navigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
//import com.example.milkymate.Store.presentation.Navigation.Navigation
//import com.example.milkymate.Store.presentation.UI.Viewmodels.AuthViewModel
import com.example.milkymate.ui.theme.MilkyMateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MilkyMateTheme {
                val authViewModel: AuthViewModel by viewModels()
                val navController = rememberNavController()

                Navigation(navController = navController, authViewModel = authViewModel)
            }
        }
        window.decorView.systemUiVisibility = (
                android.view.View.SYSTEM_UI_FLAG_IMMERSIVE
                        or android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
                        or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                )
    }
}
