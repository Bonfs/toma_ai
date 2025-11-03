package com.project.pos.tomaai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.pos.navigation.AppDestinations
import com.project.pos.onboarding.signin.SingInScreen
import com.project.pos.onboarding.signup.SignUpScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavHost()
        }
    }
}
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppDestinations.SignIn.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(AppDestinations.SignIn.route) {
            SingInScreen(
                navController = navController,
                onSignUpClick = {
                    navController.navigate(AppDestinations.SignUp.route)
                }
            )
        }

        composable(AppDestinations.SignUp.route) {
            SignUpScreen(
                navController = navController,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppNavHost()
}
