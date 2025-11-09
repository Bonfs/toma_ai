package com.project.pos.auth

interface Auth {
    fun createNewAccountWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: () -> Unit,
    )

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: () -> Unit,
    )

    fun hasSession(): Boolean

    fun token(): String?
}
