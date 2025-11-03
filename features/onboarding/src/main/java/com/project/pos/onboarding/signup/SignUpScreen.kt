package com.project.pos.onboarding.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavHostController,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Toma ai!") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
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
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    value = "",
                    onValueChange = { /* Handle text change */ },
                    label = { Text("Email") }
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    value = "",
                    onValueChange = { /* Handle text change */ },
                    label = { Text("Senha") },
                    visualTransformation = androidx . compose . ui . text . input . PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    value = "",
                    onValueChange = { /* Handle text change */ },
                    label = { Text("Confirme sua senha") },
                    visualTransformation = PasswordVisualTransformation()
                )
            }

            Spacer(modifier = Modifier.size(0.dp))

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Criar conta")
            }
        }
    }
}

@Preview
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(navController = rememberNavController())
}
