package com.project.pos.auth

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.FirebaseAuth as FirebaseSDKAuth

class FirebaseAuth(
    private val auth: FirebaseSDKAuth = Firebase.auth
) : Auth {

    override fun createNewAccountWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: () -> Unit,
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    task.exception?.let {
                        throw it
                    }
                }
            }
    }

    override fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    task.exception?.let {
                        throw it
                    }
                }
            }
    }

    override fun signOut() = auth.signOut()

    override fun hasSession(): Boolean = auth.currentUser != null

    override fun token(): String? = auth.currentUser?.uid
}
