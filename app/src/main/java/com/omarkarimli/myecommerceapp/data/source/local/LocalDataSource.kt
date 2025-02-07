package com.omarkarimli.myecommerceapp.data.source.local

import com.omarkarimli.myecommerceapp.domain.models.ProductModel

interface LocalDataSource {

    suspend fun getAllLocally(): List<ProductModel>

    suspend fun deleteProductLocally(productModel: ProductModel)

    suspend fun getProductByIdLocally(productId: Int): ProductModel?

    suspend fun addProductLocally(productModel: ProductModel)

    suspend fun updateProductLocally(productModel: ProductModel)

    suspend fun deleteAllLocally()
}