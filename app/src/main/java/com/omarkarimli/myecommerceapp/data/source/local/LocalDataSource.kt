package com.omarkarimli.myecommerceapp.data.source.local

import com.omarkarimli.myecommerceapp.domain.models.ProductModel

interface LocalDataSource {

    suspend fun getAll(): List<ProductModel>

    suspend fun delete(productModel: ProductModel)

    suspend fun getProductById(productId: Int): ProductModel?

    suspend fun addProduct(productModel: ProductModel)
}