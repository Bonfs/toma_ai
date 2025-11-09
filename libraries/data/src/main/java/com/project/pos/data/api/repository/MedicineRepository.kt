package com.project.pos.data.api.repository

import com.project.pos.data.api.models.Medicine
import kotlinx.coroutines.flow.Flow

interface MedicineRepository {
    fun addMedicine(medicine: Medicine)
    fun getMedicines(): Flow<List<Medicine>>
    fun updateMedicine(medicine: Medicine)
    fun deleteMedicine(medicineId: String)
}
