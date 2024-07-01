package com.example.milkymate

import AuthViewModel
import NavGraph
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
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
                val authViewModel: AuthViewModel = viewModel()

                NavGraph(authViewModel = authViewModel)
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
