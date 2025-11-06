package com.project.pos.onboarding.signin

data class SignInState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val isSignedIn: Boolean = false,
    val isPasswordVisible: Boolean = false
)

sealed interface SignInEvent {
    data class EmailChanged(val email: String) : SignInEvent
    data class PasswordChanged(val password: String) : SignInEvent
    object SignInClicked : SignInEvent
    object TogglePasswordVisibility : SignInEvent
}