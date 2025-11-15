package com.project.pos.home.ui

sealed interface HomeEvent {
    data class DeleteMedicine(val medicineId: String) : HomeEvent
    object ConfirmDelete : HomeEvent
    object CancelDelete : HomeEvent
    object SignOut : HomeEvent
}
