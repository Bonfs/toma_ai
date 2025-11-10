package com.project.pos.home.ui

import com.project.pos.data.api.models.Medicine

data class HomeState(
    val medicines: List<Medicine> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val medicineIdToDelete: String? = null,
    val showDeleteConfirmation: Boolean = false
)
