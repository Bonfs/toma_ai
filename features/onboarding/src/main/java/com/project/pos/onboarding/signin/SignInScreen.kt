package com.project.pos.onboarding.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun SingInScreen(
    navController: NavHostController,
    onSignUpClick: () -> Unit = {},
) {
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
            TextField(
                value = "",
                onValueChange = { /* Handle text change */ },
            )
            Spacer(modifier = Modifier.size(16.dp))
            TextField(
                value = "",
                onValueChange = { /* Handle text change */ },
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Não possui conta?")
                TextButton(
                    onClick = onSignUpClick,
                ) {
                    Text("Crie aqui")
                }
            }
        }

        Spacer(modifier = Modifier.size(0.dp))
    }
}

@Preview
@Composable
fun SingInPreview() {
    val navHost = rememberNavController()
    SingInScreen(navHost)
}
