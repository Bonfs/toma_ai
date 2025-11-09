package com.project.pos.createmedicine.ui

import java.time.LocalTime

sealed interface CreateMedicineEvent {
    data class NameChanged(val name: String) : CreateMedicineEvent
    data class TimeChanged(val time: LocalTime) : CreateMedicineEvent
    object SaveClicked : CreateMedicineEvent
}
