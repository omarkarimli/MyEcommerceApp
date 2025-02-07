package com.omarkarimli.myecommerceapp.data.repository

import com.omarkarimli.myecommerceapp.data.source.local.LocalDataSource
import com.omarkarimli.myecommerceapp.data.source.remote.RemoteDataSource
import com.omarkarimli.myecommerceapp.domain.repository.AuthRepository
import java.io.Serializable
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
): AuthRepository {

    override suspend fun loginUserAccount(isChecked: Boolean, email: String, password: String) = remoteDataSource.loginUserAccount(isChecked, email, password)

    override suspend fun registerNewUser(email: String, password: String) = remoteDataSource.registerNewUser(email, password)

    override suspend fun addUserToFirestore(userData: HashMap<String, Serializable>) = remoteDataSource.addUserToFirestore(userData)
}