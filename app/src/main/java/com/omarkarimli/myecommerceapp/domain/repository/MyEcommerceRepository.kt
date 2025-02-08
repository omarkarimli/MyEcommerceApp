package com.omarkarimli.myecommerceapp.domain.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.omarkarimli.myecommerceapp.domain.models.CategoryModel
import com.omarkarimli.myecommerceapp.domain.models.ProductModel

interface MyEcommerceRepository {

    suspend fun changePassword(email: String, currentPassword: String, newPassword: String)

    suspend fun fetchUserData(): DocumentSnapshot

    suspend fun fetchProducts(): List<ProductModel>

    suspend fun fetchBookmarkedIds(): List<Int>

    suspend fun fetchCategories(): List<CategoryModel>

    suspend fun updateBookmark(newBookmarks: MutableList<Int>)

    suspend fun getProductById(id: Int): ProductModel

    suspend fun getAllProductsFromLocal(): List<ProductModel>

    suspend fun deleteProductFromLocal(productModel: ProductModel)

    suspend fun getProductByIdFromLocal(productId: Int): ProductModel?

    suspend fun addProductToLocal(productModel: ProductModel)

    suspend fun updateProductLocally(productModel: ProductModel)

    suspend fun deleteAllProductsFromLocal()
}