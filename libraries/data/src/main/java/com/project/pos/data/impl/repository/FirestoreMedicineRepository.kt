package com.project.pos.data.impl.repository

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
    private val db = Firebase.firestore

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
                    val medicine = document.toObject<Medicine>()
                    medicine?.copy(id = document.id)
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