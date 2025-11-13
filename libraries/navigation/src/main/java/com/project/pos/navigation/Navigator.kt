package com.project.pos.navigation

interface Navigator {
    fun moveToSignUp()
    fun navigateBack()
    fun moveToSignIn()
    fun moveToHome()
    fun moveToCreateMedicine()
    fun moveToUpdateMedicine(medicineId: String)
}