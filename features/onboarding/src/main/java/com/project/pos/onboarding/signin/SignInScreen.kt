package com.project.pos.onboarding.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import org.koin.core.parameter.parametersOf

@Composable
fun SingInScreen(
    navigator: Navigator,
    viewModel: SignInViewModel = koinViewModel { parametersOf(navigator) }
) {
    val state by viewModel.state.collectAsState()
    
    SingInContent(
        state = state,
        onEvent = viewModel::onEvent,
        onMoveToSignUp = { navigator.moveToSignUp() }
    )
}

@Composable
fun SingInContent(
    state: SignInState,
    onEvent: (SignInEvent) -> Unit,
    onMoveToSignUp: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val passwordFocus = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceVariant),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            "Toma ai!",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AuthTextField(
                value = state.email,
                onValueChange = { onEvent(SignInEvent.EmailChanged(it)) },
                label = "Email",
                isError = state.emailError != null,
                supportingText = state.emailError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() })
            )
            Spacer(modifier = Modifier.size(16.dp))
            AuthTextField(
                value = state.password,
                onValueChange = { onEvent(SignInEvent.PasswordChanged(it)) },
                label = "Senha",
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = state.passwordError != null,
                supportingText = state.passwordError,
                modifier = Modifier.focusRequester(passwordFocus),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    onEvent(SignInEvent.SignInClicked)
                }),
                trailingIcon = {
                    IconButton(onClick = { onEvent(SignInEvent.TogglePasswordVisibility) }) {
                        Icon(
                            imageVector = if (state.isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (state.isPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Não possui conta?",
                    color = MaterialTheme.colorScheme.onSurface
                )
                TextButton(
                    onClick = onMoveToSignUp,
                ) {
                    Text("Crie aqui")
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { onEvent(SignInEvent.SignInClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = state.email.isNotEmpty() &&
                        state.password.isNotEmpty() &&
                        state.emailError == null &&
                        state.passwordError == null
            ) {
                Text("Entrar")
            }
        }
    }
}

@Preview
@Composable
fun SingInPreview() {
    SingInContent(
        state = SignInState(),
        onEvent = {},
        onMoveToSignUp = {}
    )
}
