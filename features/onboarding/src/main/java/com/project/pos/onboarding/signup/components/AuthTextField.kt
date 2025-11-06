package com.project.pos.onboarding.signup.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    supportingText: String?,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = isError,
        supportingText = { supportingText?.let { Text(it) } },
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon
    )
}