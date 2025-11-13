package com.project.pos.updatemedicine.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.project.pos.navigation.DefaultNavigator
import com.project.pos.navigation.Navigator

@Composable
fun UpdateMedicineScreen(
    medicineId: String?,
    navigator: Navigator
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Text("Medicine ID: $medicineId")
        }
    }
}

@Preview
@Composable
fun UpdateMedicineScreenPreview() {
    val navigator = DefaultNavigator(rememberNavController())
    UpdateMedicineScreen(
        "medicineId",
        navigator
    )
}
