package com.project.pos.onboarding.signin

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.pos.auth.Auth
import com.project.pos.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(
    private val auth: Auth,
    private val navigator: Navigator
) : ViewModel() {

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
            _state.update { it.copy(emailError = "Email não pode ser vazio") }
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _state.update { it.copy(emailError = "Email inválido") }
        } else {
            _state.update { it.copy(emailError = null) }
        }
    }

    private fun validatePassword(password: String) {
        if (password.isEmpty()) {
            _state.update { it.copy(passwordError = "Senha não pode ser vazia") }
        } else if (password.length < 8) {
            _state.update { it.copy(passwordError = "A senha deve conter pelo menos 8 caracteres") }
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
                        _state.update { it.copy(isLoading = false, isSignedIn = true) }
                        navigator.moveToHome()
                    }
                )
            }
        }
    }
}