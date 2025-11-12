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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.project.pos.auth.FirebaseAuth
import com.project.pos.createmedicine.alarm.AndroidAlarmScheduler
import com.project.pos.data.impl.repository.FirestoreMedicineRepository
import com.project.pos.navigation.DefaultNavigator
import com.project.pos.navigation.Navigator
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMedicineScreen(
    navigator: Navigator,
    viewModel: CreateMedicineViewModel = viewModel(
        factory = CreateMedicineViewModelFactory(
            FirestoreMedicineRepository(FirebaseAuth()),
            navigator,
            AndroidAlarmScheduler(LocalContext.current.applicationContext)
        )
    )
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            viewModel.onEvent(CreateMedicineEvent.TimeChanged(LocalTime.of(hourOfDay, minute)))
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
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = { viewModel.onEvent(CreateMedicineEvent.NameChanged(it)) },
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
                    onClick = { viewModel.onEvent(CreateMedicineEvent.SaveClicked) },
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
    val navigator = DefaultNavigator(rememberNavController())
    CreateMedicineScreen(navigator)
}
