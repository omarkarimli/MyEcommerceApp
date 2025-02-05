package com.omarkarimli.myecommerceapp.data.repository

import com.omarkarimli.myecommerceapp.data.source.remote.RemoteDataSource
import com.omarkarimli.myecommerceapp.domain.repository.MyEcommerceRepository
import javax.inject.Inject

class MyEcommerceRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : MyEcommerceRepository {

    override suspend fun fetchProducts() = remoteDataSource.fetchProducts()

    override suspend fun fetchCategories() = remoteDataSource.fetchCategories()

    override suspend fun fetchBookmarkedIds() = remoteDataSource.fetchBookmarkedIds()

    override suspend fun updateBookmark(newBookmarks: MutableList<Int>) = remoteDataSource.updateBookmark(newBookmarks)


    override suspend fun changePassword(currentPassword: String) = remoteDataSource.changePassword(currentPassword)

    override suspend fun fetchUserData() = remoteDataSource.fetchUserData()

    override suspend fun getProductById(id: Int) = remoteDataSource.getProductById(id)
}