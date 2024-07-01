package com.example.milkymate.Store.presentation.UI

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import com.example.milkymate.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController){
LaunchedEffect(Unit) {
    delay(1500)
    navController.navigate("LoginScreen") {
        popUpTo(navController.graph.startDestinationId) {
            inclusive = true
        }
    }
}
    Box(modifier=Modifier.fillMaxSize()){
        Image(painter = painterResource(R.drawable.logo), contentDescription = "LOGO",modifier=Modifier.fillMaxSize())

    }

}