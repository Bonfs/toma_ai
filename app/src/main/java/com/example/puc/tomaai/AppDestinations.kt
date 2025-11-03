package com.example.puc.tomaai

sealed class AppDestinations(val route: String) {
    object SignIn : AppDestinations("signin")
    object SignUp : AppDestinations("signup")
}