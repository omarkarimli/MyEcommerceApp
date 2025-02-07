package com.omarkarimli.myecommerceapp.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.omarkarimli.myecommerceapp.domain.models.ProductModel

@Dao
interface ProductDao {

    @Query("SELECT * FROM productmodel")
    suspend fun getAll(): List<ProductModel>

    @Query("DELETE FROM productmodel")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(productModel: ProductModel)

    @Query("SELECT * FROM productmodel WHERE localId = :productId")
    suspend fun getProductById(productId: Int): ProductModel?

    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun addProduct(productModel: ProductModel)

    @Update
    suspend fun updateProduct(productModel: ProductModel)
}