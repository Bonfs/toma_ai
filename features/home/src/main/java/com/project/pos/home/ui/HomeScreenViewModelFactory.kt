package com.project.pos.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.pos.data.api.repository.MedicineRepository
import com.project.pos.navigation.Navigator

class HomeScreenViewModelFactory(
    private val medicineRepository: MedicineRepository,
    private val navigator: Navigator
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeScreenViewModel(medicineRepository, navigator) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}