package com.project.pos.onboarding.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.pos.auth.Auth
import com.project.pos.navigation.Navigator

class SignInViewModelFactory(
    private val auth: Auth,
    private val navigator: Navigator
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignInViewModel(auth, navigator) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}