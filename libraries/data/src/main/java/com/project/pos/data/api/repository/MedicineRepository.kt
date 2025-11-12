package com.project.pos.data.api.repository

import com.project.pos.data.api.models.Medicine
import kotlinx.coroutines.flow.Flow

interface MedicineRepository {
    suspend fun addMedicine(medicine: Medicine): String
    fun getMedicines(): Flow<List<Medicine>>
    suspend fun updateMedicine(medicine: Medicine)
    suspend fun deleteMedicine(medicineId: String)
}
