package com.project.pos.data.impl.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.project.pos.auth.Auth
import com.project.pos.data.api.models.Medicine
import com.project.pos.data.api.repository.MedicineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FirestoreMedicineRepository(
    private val auth: Auth,
) : MedicineRepository {
    private val db by lazy { Firebase.firestore }

    override fun addMedicine(medicine: Medicine) {
        db
            .collection("users")
            .document(auth.token()!!)
            .collection("medicines")
            .add(medicine)
    }

    override fun getMedicines(): Flow<List<Medicine>> {
        return db
            .collection("users")
            .document(auth.token()!!)
            .collection("medicines")
            .snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull { document ->
                    try {
                        val data = document.data
                        val name = data?.get("name") as? String ?: ""
                        val createdAt = data?.get("createdAt") as? Long ?: 0L
                        val timeField = data?.get("time")

                        val time = when (timeField) {
                            is String -> timeField
                            is Map<*, *> -> {
                                val hour = timeField["hour"] as? Long ?: 0
                                val minute = timeField["minute"] as? Long ?: 0
                                "$hour:$minute"
                            }
                            else -> ""
                        }

                        Medicine(
                            id = document.id,
                            name = name,
                            time = time,
                            createdAt = createdAt
                        )
                    } catch (e: Exception) {
                        // Log the error or handle it as needed
                        null
                    }
                }
            }
    }

    override fun updateMedicine(medicine: Medicine) {
        db
            .collection("users")
            .document(auth.token()!!)
            .collection("medicines")
            .document(medicine.id!!)
            .set(medicine)
    }

    override fun deleteMedicine(medicineId: String) {
        db
            .collection("users")
            .document(auth.token()!!)
            .collection("medicines")
            .document(medicineId)
            .delete()
    }
}