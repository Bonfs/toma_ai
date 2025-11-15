package com.project.pos.updatemedicine.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.project.pos.createmedicine.alarm.AlarmItem
import com.project.pos.createmedicine.alarm.AlarmScheduler
import com.project.pos.data.api.models.Medicine
import com.project.pos.data.api.repository.MedicineRepository
import com.project.pos.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class UpdateMedicineViewModel(
    private val medicineId: String,
    private val medicineRepository: MedicineRepository,
    private val navigator: Navigator,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    private val _state = MutableStateFlow(UpdateMedicineState())
    val state = _state.asStateFlow()

    init {
        getMedicineDetails()
    }

    fun onEvent(event: UpdateMedicineEvent) {
        when (event) {
            is UpdateMedicineEvent.OnNameChanged -> {
                _state.update { it.copy(name = event.name, error = null) }
            }
            is UpdateMedicineEvent.OnTimeChanged -> {
                _state.update { it.copy(time = event.time) }
            }
            UpdateMedicineEvent.OnUpdateMedicineClicked -> {
                updateMedicine()
            }
        }
    }

    private fun getMedicineDetails() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val medicines = medicineRepository.getMedicines().first()
                val medicine = medicines.find { it.id == medicineId }
                if (medicine != null) {
                    val time = try {
                        LocalTime.parse(medicine.time, DateTimeFormatter.ofPattern("H:m"))
                    } catch (e: Exception) {
                        LocalTime.now()
                    }
                    _state.update {
                        it.copy(
                            medicineId = medicine.id ?: "",
                            name = medicine.name,
                            time = time,
                            isLoading = false
                        )
                    }
                } else {
                    _state.update { it.copy(isLoading = false, error = "Medicine not found") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun updateMedicine() {
        if (state.value.name.isBlank()) {
            _state.update { it.copy(error = "Name is required") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val updatedMedicine = Medicine(
                    id = state.value.medicineId,
                    name = state.value.name,
                    time = "${state.value.time.hour}:${state.value.time.minute}",
                )
                medicineRepository.updateMedicine(updatedMedicine)
                alarmScheduler.schedule(
                    AlarmItem(
                        id = updatedMedicine.id.hashCode(),
                        time = state.value.time,
                        message = "Hora de tomar o seu remédio: ${state.value.name}"
                    )
                )
                _state.update { it.copy(isLoading = false, navigateBack = true) }
                navigator.navigateBack()
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class UpdateMedicineViewModelFactory(
    private val medicineId: String,
    private val medicineRepository: MedicineRepository,
    private val navigator: Navigator,
    private val alarmScheduler: AlarmScheduler
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateMedicineViewModel::class.java)) {
            return UpdateMedicineViewModel(medicineId, medicineRepository, navigator, alarmScheduler) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
