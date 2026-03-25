package com.project.pos.data

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.project.pos.auth.Auth
import com.project.pos.data.api.models.Medicine
import com.project.pos.data.api.repository.MedicineRepository
import com.project.pos.data.impl.repository.FirestoreMedicineRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MedicineRepositoryTest {

    private lateinit var auth: Auth
    private lateinit var db: FirebaseFirestore
    private lateinit var repository: MedicineRepository

    private val userId = "test-user-id"

    @Before
    fun setUp() {
        auth = mockk()
        db = mockk()
        repository = FirestoreMedicineRepository(auth, db)

        every { auth.token() } returns userId
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `addMedicine should return document id`() = runTest {
        // Arrange
        val medicine = Medicine(name = "Paracetamol", time = "08:00", createdAt = 123456789L)
        val documentRef = mockk<DocumentReference>()
        val usersCollection = mockk<CollectionReference>()
        val userDoc = mockk<DocumentReference>()
        val medicinesCollection = mockk<CollectionReference>()
        val addTask = mockk<Task<DocumentReference>>()

        every { db.collection("users") } returns usersCollection
        every { usersCollection.document(userId) } returns userDoc
        every { userDoc.collection("medicines") } returns medicinesCollection
        every { medicinesCollection.add(medicine) } returns addTask

        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        coEvery { addTask.await() } returns documentRef
        every { documentRef.id } returns "new-medicine-id"

        // Act
        val result = repository.addMedicine(medicine)

        // Assert
        assertEquals("new-medicine-id", result)
        verify { medicinesCollection.add(medicine) }
    }

    @Test
    fun `updateMedicine should call set`() = runTest {
        // Arrange
        val medicine =
            Medicine(id = "med-id", name = "Paracetamol", time = "08:00", createdAt = 123456789L)
        val usersCollection = mockk<CollectionReference>()
        val userDoc = mockk<DocumentReference>()
        val medicinesCollection = mockk<CollectionReference>()
        val medDoc = mockk<DocumentReference>()
        val setTask = mockk<Task<Void>>()

        every { db.collection("users") } returns usersCollection
        every { usersCollection.document(userId) } returns userDoc
        every { userDoc.collection("medicines") } returns medicinesCollection
        every { medicinesCollection.document(medicine.id!!) } returns medDoc
        every { medDoc.set(medicine) } returns setTask

        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        coEvery { setTask.await() } returns mockk()

        // Act
        repository.updateMedicine(medicine)

        // Assert
        verify { medDoc.set(medicine) }
    }

    @Test
    fun `deleteMedicine should call delete`() = runTest {
        // Arrange
        val medicineId = "med-id"
        val usersCollection = mockk<CollectionReference>()
        val userDoc = mockk<DocumentReference>()
        val medicinesCollection = mockk<CollectionReference>()
        val medDoc = mockk<DocumentReference>()
        val deleteTask = mockk<Task<Void>>()

        every { db.collection("users") } returns usersCollection
        every { usersCollection.document(userId) } returns userDoc
        every { userDoc.collection("medicines") } returns medicinesCollection
        every { medicinesCollection.document(medicineId) } returns medDoc
        every { medDoc.delete() } returns deleteTask

        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        coEvery { deleteTask.await() } returns mockk()

        // Act
        repository.deleteMedicine(medicineId)

        // Assert
        verify { medDoc.delete() }
    }
}
