package com.project.pos.home.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.pos.createmedicine.alarm.AlarmScheduler
import com.project.pos.data.api.repository.MedicineRepository
import com.project.pos.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val medicineRepository: MedicineRepository,
    private val navigator: Navigator,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        getMedicines()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.DeleteMedicine -> {
                _state.update { it.copy(showDeleteConfirmation = true, medicineIdToDelete = event.medicineId) }
            }
            HomeEvent.ConfirmDelete -> {
                _state.value.medicineIdToDelete?.let { deleteMedicine(it) }
                _state.update { it.copy(showDeleteConfirmation = false, medicineIdToDelete = null) }
            }
            HomeEvent.CancelDelete -> {
                _state.update { it.copy(showDeleteConfirmation = false, medicineIdToDelete = null) }
            }
        }
    }

    private fun deleteMedicine(medicineId: String) {
        viewModelScope.launch {
            try {
                medicineRepository.deleteMedicine(medicineId)
                alarmScheduler.cancel(medicineId.hashCode())
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    private fun getMedicines() {
        viewModelScope.launch {
            medicineRepository.getMedicines()
                .onStart {
                    _state.update { it.copy(isLoading = true) }
                }
                .catch { throwable ->
                    Log.d("HomeScreenViewModel-catch", throwable.message.toString())
                    _state.update { it.copy(isLoading = false, error = throwable.message) }
                }
                .collect { medicines ->
                    Log.d("HomeScreenViewModel", "${medicines.size}")
                    _state.update { it.copy(isLoading = false, medicines = medicines) }
                }
        }
    }
}