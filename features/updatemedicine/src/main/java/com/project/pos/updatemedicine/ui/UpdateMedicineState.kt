package com.project.pos.updatemedicine.ui

import java.time.LocalTime

data class UpdateMedicineState(
    val medicineId: String = "",
    val name: String = "",
    val time: LocalTime = LocalTime.now(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val navigateBack: Boolean = false
)
