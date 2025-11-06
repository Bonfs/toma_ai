package com.project.pos.onboarding.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.pos.auth.Auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import android.util.Patterns

class SignUpViewModel(private val auth: Auth) : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    fun pb() = auth.hasSession()

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email) }
                validateEmail(event.email)
            }
            is SignUpEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password) }
                validatePassword(event.password)
                validateConfirmPassword(event.password, state.value.confirmPassword)
            }
            is SignUpEvent.ConfirmPasswordChanged -> {
                _state.update { it.copy(confirmPassword = event.confirmPassword) }
                validateConfirmPassword(state.value.password, event.confirmPassword)
            }
            SignUpEvent.TogglePasswordVisibility -> _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            SignUpEvent.SignUpClicked -> signUp()
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

    private fun validateConfirmPassword(password: String, confirmPassword: String) {
        if (password != confirmPassword) {
            _state.update { it.copy(confirmPasswordError = "Passwords do not match") }
        } else {
            _state.update { it.copy(confirmPasswordError = null) }
        }
    }

    private fun signUp() {
        validateEmail(state.value.email)
        validatePassword(state.value.password)
        validateConfirmPassword(state.value.password, state.value.confirmPassword)

        if (state.value.emailError == null &&
            state.value.passwordError == null &&
            state.value.confirmPasswordError == null &&
            state.value.email.isNotEmpty() &&
            state.value.password.isNotEmpty() &&
            state.value.confirmPassword.isNotEmpty()
        ) {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                auth.createNewAccountWithEmailAndPassword(
                    email = state.value.email,
                    password = state.value.password,
                    onSuccess = {
                        _state.update { it.copy(isLoading = false, isAccountCreated = true) }
                    }
                )
            }
        }
    }
}