package com.project.pos.createmedicine.ui

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
import java.time.LocalTime
import org.koin.core.parameter.parametersOf

@Composable
fun CreateMedicineScreen(
    navigator: Navigator,
    viewModel: CreateMedicineViewModel = koinViewModel { parametersOf(navigator) }
) {
    val state by viewModel.state.collectAsState()

    CreateMedicineScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = { navigator.navigateBack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateMedicineScreenContent(
    state: CreateMedicineState,
    onEvent: (CreateMedicineEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            onEvent(CreateMedicineEvent.TimeChanged(LocalTime.of(hourOfDay, minute)))
        },
        state.time.hour,
        state.time.minute,
        true
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar medicamento") },
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
            OutlinedTextField(
                value = state.name,
                onValueChange = { onEvent(CreateMedicineEvent.NameChanged(it)) },
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
                    onClick = { onEvent(CreateMedicineEvent.SaveClicked) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Salvar")
                }
            }
        }
    }
}

@Preview
@Composable
fun CreateMedicineScreenPreview() {
    CreateMedicineScreenContent(
        state = CreateMedicineState(
            name = "Paracetamol",
            time = LocalTime.of(12, 0)
        ),
        onEvent = {},
        onBackClick = {}
    )
}
