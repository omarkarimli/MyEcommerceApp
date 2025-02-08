package com.omarkarimli.myecommerceapp.data.repository

import com.omarkarimli.myecommerceapp.data.source.local.LocalDataSource
import com.omarkarimli.myecommerceapp.data.source.remote.RemoteDataSource
import com.omarkarimli.myecommerceapp.domain.models.ProductModel
import com.omarkarimli.myecommerceapp.domain.repository.MyEcommerceRepository
import javax.inject.Inject

class MyEcommerceRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : MyEcommerceRepository {

    // Remote
    override suspend fun fetchProducts() = remoteDataSource.fetchProducts()

    override suspend fun fetchCategories() = remoteDataSource.fetchCategories()

    override suspend fun fetchBookmarkedIds() = remoteDataSource.fetchBookmarkedIds()

    override suspend fun updateBookmark(newBookmarks: MutableList<Int>) = remoteDataSource.updateBookmark(newBookmarks)

    override suspend fun changePassword(email: String, currentPassword: String, newPassword: String) = remoteDataSource.changePassword(email, currentPassword, newPassword)

    override suspend fun fetchUserData() = remoteDataSource.fetchUserData()

    override suspend fun getProductById(id: Int) = remoteDataSource.getProductById(id)

    // Local
    override suspend fun getAllProductsFromLocal() = localDataSource.getAllLocally()

    override suspend fun deleteProductFromLocal(productModel: ProductModel) = localDataSource.deleteProductLocally(productModel)

    override suspend fun getProductByIdFromLocal(productId: Int) = localDataSource.getProductByIdLocally(productId)

    override suspend fun addProductToLocal(productModel: ProductModel) = localDataSource.addProductLocally(productModel)

    override suspend fun updateProductLocally(productModel: ProductModel) = localDataSource.updateProductLocally(productModel)

    override suspend fun deleteAllProductsFromLocal() = localDataSource.deleteAllLocally()
}