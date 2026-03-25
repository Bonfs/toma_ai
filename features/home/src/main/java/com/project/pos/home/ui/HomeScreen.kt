package com.project.pos.home.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.project.pos.data.api.models.Medicine
import com.project.pos.navigation.DefaultNavigator
import com.project.pos.navigation.Navigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigator: Navigator,
    viewModel: HomeScreenViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(viewModel::onEvent)
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Toma ai!") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (drawerState.isClosed) {
                                        drawerState.open()
                                    } else {
                                        drawerState.close()
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
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
                state.isLoading -> LoadingMedicinesContent()
                state.showDeleteConfirmation -> DeleteMedicineDialog(viewModel::onEvent)
                else -> HomeScreenContent(
                    paddingValues,
                    viewModel::onEvent,
                    navigator,
                    state.medicines
                )
            }
        }
    }
}

@Composable
private fun DeleteMedicineDialog(onEvent: (HomeEvent) -> Unit) {
    AlertDialog(
        title = {
            Text(text = "Apagar medicamento?")
        },
        text = {
            Text(text = "Tem certeza que deseja apagar esse medicamento?")
        },
        onDismissRequest = { onEvent(HomeEvent.CancelDelete) },
        confirmButton = {
            TextButton(
                onClick = {
                    onEvent(HomeEvent.ConfirmDelete)
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onEvent(HomeEvent.CancelDelete) }
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun LoadingMedicinesContent() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun DrawerContent(onEvent: (HomeEvent) -> Unit) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier.run {
                padding(horizontal = 16.dp).verticalScroll(rememberScrollState())
            }
        ) {
            Spacer(Modifier.height(12.dp))
            Text("Perfil")
            HorizontalDivider()

            NavigationDrawerItem(
                label = { Text("Sair") },
                selected = false,
                onClick = { onEvent(HomeEvent.SignOut) }
            )
        }
    }
}

@Composable
private fun HomeScreenContent(
    paddingValues: PaddingValues,
    onEvent: (HomeEvent) -> Unit,
    navigator: Navigator,
    medicines: List<Medicine>
) {
    if (medicines.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "Nenhum Medicamento Adicionado",
                style = MaterialTheme.typography.titleLarge
            )
        }
    } else {
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
                        onEvent(HomeEvent.DeleteMedicine(medicine.id!!))
                    },
                    onEditClick = {
                        navigator.moveToUpdateMedicine(medicine.id!!)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenWithEmptyMedicinePreview() {
    val navigator = DefaultNavigator(rememberNavController())
    Scaffold { paddingValues ->
        HomeScreenContent(
            paddingValues = paddingValues,
            onEvent = {},
            navigator = navigator,
            medicines = listOf()
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenWithPreview() {
    val navigator = DefaultNavigator(rememberNavController())
    Scaffold { paddingValues ->
        HomeScreenContent(
            paddingValues = paddingValues,
            onEvent = {},
            navigator = navigator,
            medicines = listOf(
                Medicine(
                    id = "1",
                    name = "Medicamento 1",
                    time = "12:00",
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DeleteMedicineDialogPreview() {
    DeleteMedicineDialog(onEvent = {})
}

@Preview(showBackground = true)
@Composable
fun LoadingMedicinesContentPreview() {
    LoadingMedicinesContent()
}

