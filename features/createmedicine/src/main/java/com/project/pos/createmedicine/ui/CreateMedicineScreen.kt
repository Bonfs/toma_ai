package com.project.pos.createmedicine.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.project.pos.navigation.DefaultNavigator
import com.project.pos.navigation.Navigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMedicineScreen(
    navigator: Navigator,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Toma ai!") },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateBack() }) {
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
            modifier = Modifier.padding(paddingValues)
        ) {

        }
    }
}

@Preview
@Composable
fun CreateMedicineScreenPreview() {
    val navigator = DefaultNavigator(rememberNavController())
    CreateMedicineScreen(navigator)
}
