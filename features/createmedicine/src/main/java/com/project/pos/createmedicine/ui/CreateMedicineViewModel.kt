package com.project.pos.createmedicine.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.pos.data.api.models.Medicine
import com.project.pos.data.api.repository.MedicineRepository
import com.project.pos.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime

class CreateMedicineViewModel(
    private val medicineRepository: MedicineRepository,
    private val navigator: Navigator
) : ViewModel() {

    private val _state = MutableStateFlow(CreateMedicineState())
    val state = _state.asStateFlow()

    fun onEvent(event: CreateMedicineEvent) {
        when (event) {
            is CreateMedicineEvent.NameChanged -> {
                _state.update { it.copy(name = event.name, error = null) }
            }
            is CreateMedicineEvent.TimeChanged -> {
                _state.update { it.copy(time = event.time) }
            }
            CreateMedicineEvent.SaveClicked -> {
                saveMedicine()
            }
        }
    }

    private fun saveMedicine() {
        if (state.value.name.isBlank()) {
            _state.update { it.copy(error = "Name is required") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                medicineRepository.addMedicine(
                    Medicine(
                        name = state.value.name,
                        time = state.value.time,
                        createdAt = System.currentTimeMillis()
                    )
                )
                _state.update { it.copy(isLoading = false, isMedicineCreated = true) }
                navigator.navigateBack()
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
