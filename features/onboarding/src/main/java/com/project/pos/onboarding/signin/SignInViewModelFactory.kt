package com.project.pos.onboarding.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.pos.auth.Auth

class SignInViewModelFactory(private val auth: Auth) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignInViewModel(auth) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}