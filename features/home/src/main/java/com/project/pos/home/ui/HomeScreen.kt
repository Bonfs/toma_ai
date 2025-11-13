package com.project.pos.home.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.project.pos.auth.FirebaseAuth
import com.project.pos.createmedicine.alarm.AndroidAlarmScheduler
import com.project.pos.data.api.models.Medicine
import com.project.pos.data.impl.repository.FirestoreMedicineRepository
import com.project.pos.navigation.DefaultNavigator
import com.project.pos.navigation.Navigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigator: Navigator,
    viewModel: HomeScreenViewModel = viewModel(
        factory = HomeScreenViewModelFactory(
            FirestoreMedicineRepository(FirebaseAuth()),
            navigator,
            AndroidAlarmScheduler(LocalContext.current.applicationContext)
        )
    )
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Toma ai!") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigator.moveToCreateMedicine() }
            ) {
                Text("+")
            }
        }
    ) { paddingValues ->
        when {
            state.isLoading -> Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                CircularProgressIndicator()
            }
            state.showDeleteConfirmation -> {
                AlertDialog(
                        title = {
                            Text(text = "Apagar medicamento?")
                        },
                        text = {
                            Text(text = "Tem certeza que deseja apagar esse medicamento?")
                        },
                        onDismissRequest = { viewModel.onEvent(HomeEvent.CancelDelete) },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.onEvent(HomeEvent.ConfirmDelete)
                                }
                            ) {
                                Text("Confirmar")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { viewModel.onEvent(HomeEvent.CancelDelete) }
                            ) {
                                Text("Cancelar")
                            }
                        }
                    )
            }
            else -> {
                HomeScreenContent(
                    paddingValues,
                    viewModel,
                    navigator,
                    state.medicines
                )
            }
        }
    }
}

@Composable
private fun HomeScreenContent(
    paddingValues: PaddingValues,
    viewModel: HomeScreenViewModel,
    navigator: Navigator,
    medicines: List<Medicine>
) {
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(medicines) { medicine ->
            MedicineItem(
                medicine,
                onDeleteClick = {
                    viewModel.onEvent(HomeEvent.DeleteMedicine(medicine.id!!))
                },
                onEditClick = {
                    navigator.moveToUpdateMedicine(medicine.id!!)
                }
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    val navigator = DefaultNavigator(rememberNavController())
    HomeScreen(navigator)
}

