package com.project.pos.tomaai

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.pos.auth.Auth
import com.project.pos.auth.FirebaseAuth
import com.project.pos.createmedicine.alarm.AlarmReceiver
import com.project.pos.createmedicine.ui.CreateMedicineScreen
import com.project.pos.design_system.theme.TomaAiTheme
import com.project.pos.home.ui.HomeScreen
import com.project.pos.navigation.AppDestinations
import com.project.pos.navigation.DefaultNavigator
import com.project.pos.navigation.Navigator
import com.project.pos.onboarding.signin.SingInScreen
import com.project.pos.onboarding.signup.SignUpScreen

class MainActivity : ComponentActivity() {
    private val auth: Auth by lazy {
        FirebaseAuth()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        scheduleAlarm()
        setContent {
            val navController = rememberNavController()
            Log.d("MainActivity", "onCreate: ${auth.hasSession()}")
            TomaAiTheme {
                AppNavHost(
                    navController,
                    startDestination = if (auth.hasSession()) AppDestinations.Home.route else AppDestinations.SignIn.route
                )
            }
        }
    }

    private fun scheduleAlarm() {
        val alarmManager = this.getSystemService(AlarmManager::class.java)

        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerAtMillis = System.currentTimeMillis() + 10000
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppDestinations.SignIn.route,
) {
    val navigator: Navigator = remember { DefaultNavigator(navController) }
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(AppDestinations.SignIn.route) {
            SingInScreen(
                navigator,
            )
        }

        composable(AppDestinations.SignUp.route) {
            SignUpScreen(
                navigator,
            )
        }
        composable(AppDestinations.Home.route) {
            HomeScreen(
                navigator,
            )
        }
        composable(AppDestinations.CreateMedicine.route) {
            CreateMedicineScreen(
                navigator
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppNavHost()
}
