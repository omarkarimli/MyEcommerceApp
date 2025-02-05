package com.omarkarimli.myecommerceapp.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.omarkarimli.myecommerceapp.domain.models.ProductModel

@Dao
interface ProductDao {

    @Query("SELECT * FROM productmodel")
    suspend fun getAll(): List<ProductModel>

    @Delete
    suspend fun delete(productModel: ProductModel)

    @Query("SELECT * FROM productmodel WHERE id = :productId")
    suspend fun getProductById(productId: Int): ProductModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduct(productModel: ProductModel)
}