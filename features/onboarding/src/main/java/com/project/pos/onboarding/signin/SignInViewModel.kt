package com.project.pos.onboarding.signin

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.pos.auth.Auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(private val auth: Auth) : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email) }
                validateEmail(event.email)
            }
            is SignInEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password) }
                validatePassword(event.password)
            }
            SignInEvent.SignInClicked -> signIn()
            SignInEvent.TogglePasswordVisibility -> _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
        }
    }

    private fun validateEmail(email: String) {
        if (email.isEmpty()) {
            _state.update { it.copy(emailError = "Email can't be empty") }
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _state.update { it.copy(emailError = "Invalid email format") }
        } else {
            _state.update { it.copy(emailError = null) }
        }
    }

    private fun validatePassword(password: String) {
        if (password.isEmpty()) {
            _state.update { it.copy(passwordError = "Password can't be empty") }
        } else if (password.length < 8) {
            _state.update { it.copy(passwordError = "Password must have at least 8 characters") }
        } else {
            _state.update { it.copy(passwordError = null) }
        }
    }

    private fun signIn() {
        validateEmail(state.value.email)
        validatePassword(state.value.password)

        if (state.value.emailError == null && state.value.passwordError == null) {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                auth.signInWithEmailAndPassword(
                    email = state.value.email,
                    password = state.value.password,
                    onSuccess = {
                        println("success")
                        _state.update { it.copy(isLoading = false, isSignedIn = true) }
                    }
                )
            }
        }
    }
}