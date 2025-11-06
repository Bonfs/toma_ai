package com.project.pos.onboarding.signup

import com.project.pos.auth.Auth

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isAccountCreated: Boolean = false
)

sealed interface SignUpEvent {
    data class EmailChanged(val email: String) : SignUpEvent
    data class PasswordChanged(val password: String) : SignUpEvent
    data class ConfirmPasswordChanged(val confirmPassword: String) : SignUpEvent
    object TogglePasswordVisibility : SignUpEvent
    object SignUpClicked : SignUpEvent
}