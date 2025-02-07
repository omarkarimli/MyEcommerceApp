package com.omarkarimli.myecommerceapp.data.source.local

import com.omarkarimli.myecommerceapp.domain.models.ProductModel
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val productDao: ProductDao
) : LocalDataSource {

    override suspend fun getAllLocally() = productDao.getAll()

    override suspend fun deleteProductLocally(productModel: ProductModel) = productDao.delete(productModel)

    override suspend fun getProductByIdLocally(productId: Int) = productDao.getProductById(productId)

    override suspend fun addProductLocally(productModel: ProductModel) = productDao.addProduct(productModel)

    override suspend fun updateProductLocally(productModel: ProductModel) = productDao.updateProduct(productModel)

    override suspend fun deleteAllLocally() = productDao.deleteAll()
}