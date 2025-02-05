package com.omarkarimli.myecommerceapp.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.omarkarimli.myecommerceapp.domain.repository.AuthRepository
import com.omarkarimli.myecommerceapp.utils.Constants
import kotlinx.coroutines.tasks.await
import java.io.Serializable
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val provideAuth: FirebaseAuth,
    private val provideFirestore: FirebaseFirestore
): AuthRepository {

    override suspend fun loginUserAccount(isChecked: Boolean, email: String, password: String): AuthResult =
        provideAuth
            .signInWithEmailAndPassword(email, password)
            .await()

    override suspend fun registerNewUser(email: String, password: String): AuthResult =
        provideAuth
            .createUserWithEmailAndPassword(email, password)
            .await()

    override suspend fun addUserToFirestore(userData: HashMap<String, Serializable>) {
        val uid = provideAuth.currentUser?.uid ?: "error"
        provideFirestore
            .collection(Constants.USERS)
            .document(uid)
            .set(userData)
            .await()
    }
}