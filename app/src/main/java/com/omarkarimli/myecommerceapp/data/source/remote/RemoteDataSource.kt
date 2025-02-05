package com.omarkarimli.myecommerceapp.data.source.remote

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.omarkarimli.myecommerceapp.domain.models.ProductModel

interface RemoteDataSource {

    suspend fun changePassword(currentPassword: String) : Void?

    suspend fun fetchUserData(): DocumentSnapshot

    suspend fun fetchProducts(): QuerySnapshot

    suspend fun fetchBookmarkedIds(): List<Int>

    suspend fun fetchCategories(): QuerySnapshot

    suspend fun updateBookmark(newBookmarks: MutableList<Int>)

    suspend fun getProductById(id: Int): ProductModel
}