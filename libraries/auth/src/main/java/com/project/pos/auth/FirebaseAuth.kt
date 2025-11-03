package com.project.pos.auth

import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class FirebaseAuth() : Auth {
    private val auth by lazy {
        Firebase.auth
    }

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

    override fun hasSession(): Boolean = auth.currentUser != null
}
