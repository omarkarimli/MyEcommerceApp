package com.omarkarimli.myecommerceapp.data.source.local

import com.omarkarimli.myecommerceapp.domain.models.ProductModel
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val productDao: ProductDao
) : LocalDataSource {
    override suspend fun getAll(): List<ProductModel> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(productModel: ProductModel) {
        TODO("Not yet implemented")
    }

    override suspend fun getProductById(productId: Int): ProductModel? {
        TODO("Not yet implemented")
    }

    override suspend fun addProduct(productModel: ProductModel) {
        TODO("Not yet implemented")
    }
}