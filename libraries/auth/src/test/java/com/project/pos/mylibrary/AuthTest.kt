package com.project.pos.mylibrary

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuth as FirebaseSDKAuth
import com.project.pos.auth.Auth
import com.project.pos.auth.FirebaseAuth
import io.mockk.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AuthTest {
    private lateinit var firebaseSDKAuth: FirebaseSDKAuth
    private lateinit var auth: Auth

    @Before
    fun setUp() {
        firebaseSDKAuth = mockk(relaxed = true)
        auth = FirebaseAuth(firebaseSDKAuth)
    }

    @Test
    fun `createNewAccountWithEmailAndPassword should call onSuccess when successful`() {
        // Arrange
        val email = "test@example.com"
        val password = "password"
        val onSuccess = mockk<() -> Unit>(relaxed = true)
        val task = mockk<Task<AuthResult>>()
        
        every { firebaseSDKAuth.createUserWithEmailAndPassword(email, password) } returns task
        every { task.isSuccessful } returns true
        
        val slot = slot<OnCompleteListener<AuthResult>>()
        every { task.addOnCompleteListener(capture(slot)) } returns task
        
        // Act
        auth.createNewAccountWithEmailAndPassword(email, password, onSuccess)
        slot.captured.onComplete(task)
        
        // Assert
        verify { onSuccess() }
    }

    @Test(expected = Exception::class)
    fun `createNewAccountWithEmailAndPassword should throw exception when failed`() {
        // Arrange
        val email = "test@example.com"
        val password = "password"
        val onSuccess = mockk<() -> Unit>(relaxed = true)
        val task = mockk<Task<AuthResult>>()
        val exception = Exception("Failed")
        
        every { firebaseSDKAuth.createUserWithEmailAndPassword(email, password) } returns task
        every { task.isSuccessful } returns false
        every { task.exception } returns exception
        
        val slot = slot<OnCompleteListener<AuthResult>>()
        every { task.addOnCompleteListener(capture(slot)) } returns task
        
        // Act
        auth.createNewAccountWithEmailAndPassword(email, password, onSuccess)
        slot.captured.onComplete(task)
        
        // Assert
        verify(exactly = 0) { onSuccess() }
    }

    @Test
    fun `signInWithEmailAndPassword should call onSuccess when successful`() {
        // Arrange
        val email = "test@example.com"
        val password = "password"
        val onSuccess = mockk<() -> Unit>(relaxed = true)
        val task = mockk<Task<AuthResult>>()
        
        every { firebaseSDKAuth.signInWithEmailAndPassword(email, password) } returns task
        every { task.isSuccessful } returns true
        
        val slot = slot<OnCompleteListener<AuthResult>>()
        every { task.addOnCompleteListener(capture(slot)) } returns task
        
        // Act
        auth.signInWithEmailAndPassword(email, password, onSuccess)
        slot.captured.onComplete(task)
        
        // Assert
        verify { onSuccess() }
    }

    @Test(expected = Exception::class)
    fun `signInWithEmailAndPassword should throw exception when failed`() {
        // Arrange
        val email = "test@example.com"
        val password = "password"
        val onSuccess = mockk<() -> Unit>(relaxed = true)
        val task = mockk<Task<AuthResult>>()
        val exception = Exception("Failed")
        
        every { firebaseSDKAuth.signInWithEmailAndPassword(email, password) } returns task
        every { task.isSuccessful } returns false
        every { task.exception } returns exception
        
        val slot = slot<OnCompleteListener<AuthResult>>()
        every { task.addOnCompleteListener(capture(slot)) } returns task
        
        // Act
        auth.signInWithEmailAndPassword(email, password, onSuccess)
        slot.captured.onComplete(task)
        
        // Assert
        verify(exactly = 0) { onSuccess() }
    }

    @Test
    fun `signOut should call firebaseSDKAuth signOut`() {
        // Act
        auth.signOut()
        
        // Assert
        verify { firebaseSDKAuth.signOut() }
    }

    @Test
    fun `hasSession should return true when currentUser is not null`() {
        // Arrange
        val user = mockk<FirebaseUser>()
        every { firebaseSDKAuth.currentUser } returns user
        
        // Act
        val result = auth.hasSession()
        
        // Assert
        assertTrue(result)
    }

    @Test
    fun `hasSession should return false when currentUser is null`() {
        // Arrange
        every { firebaseSDKAuth.currentUser } returns null
        
        // Act
        val result = auth.hasSession()
        
        // Assert
        assertFalse(result)
    }

    @Test
    fun `token should return uid when currentUser is not null`() {
        // Arrange
        val user = mockk<FirebaseUser>()
        every { user.uid } returns "test-uid"
        every { firebaseSDKAuth.currentUser } returns user
        
        // Act
        val result = auth.token()
        
        // Assert
        assertEquals("test-uid", result)
    }

    @Test
    fun `token should return null when currentUser is null`() {
        // Arrange
        every { firebaseSDKAuth.currentUser } returns null
        
        // Act
        val result = auth.token()
        
        // Assert
        assertNull(result)
    }
}
