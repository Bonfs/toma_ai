package com.project.pos.createmedicine.ui

import java.time.LocalTime

data class CreateMedicineState(
    val name: String = "",
    val time: LocalTime = LocalTime.now(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isMedicineCreated: Boolean = false
)
