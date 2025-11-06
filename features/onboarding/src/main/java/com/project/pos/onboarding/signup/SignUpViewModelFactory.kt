package com.project.pos.onboarding.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.pos.auth.Auth

class SignUpViewModelFactory(private val auth: Auth) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignUpViewModel(auth) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}