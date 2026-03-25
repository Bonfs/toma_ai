package com.project.pos.updatemedicine.ui

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.pos.navigation.Navigator
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateMedicineScreen(
    medicineId: String?,
    navigator: Navigator,
    viewModel: UpdateMedicineViewModel = koinViewModel { parametersOf(medicineId, navigator) }
) {
    if (medicineId == null) {
        // Handle error case where medicineId is null
        Text("Error: Medicine ID is missing.")
        return
    }

    val state by viewModel.state.collectAsState()

    UpdateMedicineScreenContent(
        state = state,
        onEvent = { viewModel.onEvent(it) },
        onBackClick = { navigator.navigateBack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UpdateMedicineScreenContent(
    state: UpdateMedicineState,
    onEvent: (UpdateMedicineEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            onEvent(UpdateMedicineEvent.OnTimeChanged(LocalTime.of(hourOfDay, minute)))
        },
        state.time.hour,
        state.time.minute,
        true
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar medicamento") },
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
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (state.isLoading && state.name.isBlank()) {
                CircularProgressIndicator()
            } else {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { onEvent(UpdateMedicineEvent.OnNameChanged(it)) },
                    label = { Text("Nome do remédio") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.error != null,
                    supportingText = {
                        if (state.error != null) {
                            Text(text = state.error ?: "")
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { timePickerDialog.show() }) {
                    Text(text = "Horário: ${state.time.hour}:${state.time.minute}")
                }
                Spacer(modifier = Modifier.height(32.dp))

                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = { onEvent(UpdateMedicineEvent.OnUpdateMedicineClicked) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Salvar Alterações")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun UpdateMedicineScreenPreview() {
    UpdateMedicineScreenContent(
        state = UpdateMedicineState(
            name = "Paracetamol",
            time = LocalTime.of(10, 0)
        ),
        onEvent = {},
        onBackClick = {}
    )
}
