package com.example.milkymate.Store.presentation.UI

import AuthViewModel
import HomeScreen
import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.milkymate.R
import com.example.milkymate.ui.theme.dimens
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.serialization.json.Json
import java.net.URLEncoder

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = viewModel(),
    navController: NavController
) {
    authViewModel.setNavController(navController)


    val context = LocalContext.current
    val googleSignInClient = remember {
        GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { idToken ->
                authViewModel.loginWithGoogle(idToken)
            }
        } catch (e: ApiException) {
            Toast.makeText(context, "Sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

  /*  val authState by authViewModel.authState.observeAsState()

    if (authState == AuthState.Authenticated) {

       // HomeScreen(navController = navController, user = user, viewModel = viewModel())
       // authViewModel.navigateToHomeScreen(navController=navController, user = )

    } else {
        PotraitLoginScreen(launcher = launcher, googleSignInClient = googleSignInClient, navController = navController)
    }

   */
    PotraitLoginScreen(launcher = launcher, googleSignInClient = googleSignInClient,navController=navController)

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun PotraitLoginScreen(
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    googleSignInClient: GoogleSignInClient,
    navController: NavController
) {
    val font1 = FontFamily(Font(R.font.font1))
    val font2 = FontFamily(Font(R.font.font2))
    val font3 = FontFamily(Font(R.font.font3))
    val screenHeight = LocalConfiguration.current.screenHeightDp
    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .size(310.dp)
                )
                Spacer(modifier = Modifier.padding(MaterialTheme.dimens.medium1))

                Text(
                    text = "Welcome Back",
                    fontSize = 45.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = font2,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.padding(MaterialTheme.dimens.small3))

                Card(
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.medium1)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(Color.LightGray)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.dimens.medium1)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "Get Started",
                                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                                modifier = Modifier.padding(MaterialTheme.dimens.small3),
                                fontWeight = FontWeight.Bold,
                                fontFamily = font1,
                                color = Color.DarkGray
                            )


                            Text(
                                "This is a Password-less Authentication ",
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                fontFamily = font2,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.padding(MaterialTheme.dimens.small1))

                            Text(
                                "By Google Authentication ",
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                fontFamily = font2,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.padding(MaterialTheme.dimens.small2))

                            Button(
                                onClick = {
                                    launcher.launch(googleSignInClient.signInIntent)

                                },
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.width(200.dp),
                                elevation = ButtonDefaults.buttonElevation(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            ) {
                                Row {
                                    Image(
                                        painter = painterResource(R.drawable.googlelogo),
                                        contentDescription = "Google logo",
                                        modifier = Modifier.size(35.dp)
                                    )
                                    Spacer(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small2))

                                    Text(
                                        "Continue",
                                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                        fontFamily = font2,
                                        color = Color.Black,
                                        fontWeight = FontWeight(800)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
