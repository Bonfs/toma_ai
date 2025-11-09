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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.project.pos.auth.FirebaseAuth
import com.project.pos.design_system.components.textfield.AuthTextField
import com.project.pos.navigation.DefaultNavigator
import com.project.pos.navigation.Navigator

@Composable
fun SingInScreen(
    navigator: Navigator,
    viewModel: SignInViewModel = viewModel(
        factory = SignInViewModelFactory(FirebaseAuth(), navigator),
    )
) {
    val state by viewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current
    val passwordFocus = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Toma ai!")

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AuthTextField(
                value = state.email,
                onValueChange = { viewModel.onEvent(SignInEvent.EmailChanged(it)) },
                label = "Email",
                isError = state.emailError != null,
                supportingText = state.emailError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() })
            )
            Spacer(modifier = Modifier.size(16.dp))
            AuthTextField(
                value = state.password,
                onValueChange = { viewModel.onEvent(SignInEvent.PasswordChanged(it)) },
                label = "Senha",
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = state.passwordError != null,
                supportingText = state.passwordError,
                modifier = Modifier.focusRequester(passwordFocus),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    viewModel.onEvent(SignInEvent.SignInClicked)
                }),
                trailingIcon = {
                    IconButton(onClick = { viewModel.onEvent(SignInEvent.TogglePasswordVisibility) }) {
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
                Text("Não possui conta?")
                TextButton(
                    onClick = { navigator.moveToSignUp() },
                ) {
                    Text("Crie aqui")
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { viewModel.onEvent(SignInEvent.SignInClicked) },
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
    val nav = DefaultNavigator(rememberNavController())
    SingInScreen(nav)
}
