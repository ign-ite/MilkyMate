package com.example.milkymate.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.milkymate.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(){
LaunchedEffect(Unit) {
    delay(1500)
}
    Box(modifier=Modifier.fillMaxSize()){
        Image(painter = painterResource(R.drawable.logo), contentDescription = "LOGO",modifier=Modifier.fillMaxSize())

    }

}