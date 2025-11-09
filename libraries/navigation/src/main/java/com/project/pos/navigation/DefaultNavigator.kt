package com.project.pos.navigation

import androidx.navigation.NavHostController

class DefaultNavigator(
    val navController: NavHostController,
): Navigator {
    override fun moveToSignUp() {
        navController.navigate(AppDestinations.SignUp.route)
    }

    override fun navigateBack() {
        navController.navigateUp()
    }

    override fun moveToSignIn() {
        navController.navigate(AppDestinations.SignIn.route)
    }

    override fun moveToHome() {
        navController.navigate(AppDestinations.Home.route) {
            popUpTo(0)
        }
    }

    override fun moveToCreateMedicine() {
        navController.navigate(AppDestinations.CreateMedicine.route)
    }
}