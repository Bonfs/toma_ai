package com.project.pos.updatemedicine.ui

import java.time.LocalTime

sealed interface UpdateMedicineEvent {
    data class OnNameChanged(val name: String) : UpdateMedicineEvent
    data class OnTimeChanged(val time: LocalTime) : UpdateMedicineEvent
    data object OnUpdateMedicineClicked : UpdateMedicineEvent
}
