package com.omarkarimli.myecommerceapp.domain.repository

import com.google.firebase.auth.AuthResult
import java.io.Serializable

interface AuthRepository {

    suspend fun loginUserAccount(isChecked: Boolean, email: String, password: String): AuthResult

    suspend fun registerNewUser(email: String, password: String): AuthResult

    suspend fun addUserToFirestore(userData: HashMap<String, Serializable>)
}