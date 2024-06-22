package com.example.milkymate.ui.theme

import androidx.browser.trusted.ScreenOrientation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import com.example.milkymate.Screens.CompactDimens
import com.example.milkymate.Screens.Dimens

@Composable
fun AppUtils(
    appDimens: Dimens,
    context: @Composable () -> Unit
){
    val appDimens = remember{
        appDimens
    }

    CompositionLocalProvider(LocalAppDimens provides appDimens) {
        context()
    }

}

val LocalAppDimens = compositionLocalOf { CompactDimens }



