package com.omarkarimli.myecommerceapp.data.source.remote

import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.omarkarimli.myecommerceapp.domain.models.CategoryModel
import com.omarkarimli.myecommerceapp.domain.models.ProductModel
import com.omarkarimli.myecommerceapp.utils.Constants
import kotlinx.coroutines.tasks.await
import java.io.Serializable

interface RemoteDataSource {

    suspend fun changePassword(currentPassword: String) : Void?

    suspend fun fetchUserData(): DocumentSnapshot

    suspend fun fetchProducts(): List<ProductModel>

    suspend fun fetchBookmarkedIds(): List<Int>

    suspend fun fetchCategories(): List<CategoryModel>

    suspend fun updateBookmark(newBookmarks: MutableList<Int>)

    suspend fun getProductById(id: Int): ProductModel

    suspend fun loginUserAccount(isChecked: Boolean, email: String, password: String): AuthResult

    suspend fun registerNewUser(email: String, password: String): AuthResult

    suspend fun addUserToFirestore(userData: HashMap<String, Serializable>)
}