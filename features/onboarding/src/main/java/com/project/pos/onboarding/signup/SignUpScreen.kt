package com.project.pos.onboarding.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.pos.design_system.components.textfield.AuthTextField
import com.project.pos.navigation.Navigator
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreen(
    navigator: Navigator,
    viewModel: SignUpViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    SignUpScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = { navigator.navigateBack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignUpScreenContent(
    state: SignUpState,
    onEvent: (SignUpEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val (passwordFocus, confirmPasswordFocus) = remember { FocusRequester.createRefs() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Toma ai!") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = MaterialTheme.colorScheme.surfaceVariant),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AuthTextField(
                    value = state.email,
                    onValueChange = { onEvent(SignUpEvent.EmailChanged(it)) },
                    label = "Email",
                    isError = state.emailError != null,
                    supportingText = state.emailError,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() })
                )
                Spacer(modifier = Modifier.size(16.dp))

                AuthTextField(
                    value = state.password,
                    onValueChange = { onEvent(SignUpEvent.PasswordChanged(it)) },
                    label = "Senha",
                    visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = state.passwordError != null,
                    supportingText = state.passwordError,
                    modifier = Modifier.focusRequester(passwordFocus),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { confirmPasswordFocus.requestFocus() }),
                    trailingIcon = {
                        IconButton(onClick = { onEvent(SignUpEvent.TogglePasswordVisibility) }) {
                            Icon(
                                imageVector = if (state.isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (state.isPasswordVisible) "Hide password" else "Show password"
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.size(16.dp))
                AuthTextField(
                    value = state.confirmPassword,
                    onValueChange = { onEvent(SignUpEvent.ConfirmPasswordChanged(it)) },
                    label = "Confirme sua senha",
                    visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = state.confirmPasswordError != null,
                    supportingText = state.confirmPasswordError,
                    modifier = Modifier.focusRequester(confirmPasswordFocus),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        onEvent(SignUpEvent.SignUpClicked)
                    }),
                    trailingIcon = {
                        IconButton(onClick = { onEvent(SignUpEvent.TogglePasswordVisibility) }) {
                            Icon(
                                imageVector = if (state.isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (state.isPasswordVisible) "Hide password" else "Show password"
                            )
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.size(0.dp))

            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { onEvent(SignUpEvent.SignUpClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    enabled = state.email.isNotEmpty() &&
                        state.password.isNotEmpty() &&
                        state.confirmPassword.isNotEmpty() &&
                        state.emailError == null &&
                        state.passwordError == null &&
                        state.confirmPasswordError == null
                ) {
                    Text("Criar conta")
                }
            }
        }
    }
}

@Preview
@Composable
fun SignUpScreenPreview() {
    SignUpScreenContent(
        state = SignUpState(),
        onEvent = {},
        onBackClick = {}
    )
}
