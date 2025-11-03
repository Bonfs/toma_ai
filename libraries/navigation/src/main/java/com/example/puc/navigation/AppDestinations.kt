package com.example.puc.navigation

sealed class AppDestinations(val route: String) {
    object SignIn : AppDestinations("signin")
    object SignUp : AppDestinations("signup")
}