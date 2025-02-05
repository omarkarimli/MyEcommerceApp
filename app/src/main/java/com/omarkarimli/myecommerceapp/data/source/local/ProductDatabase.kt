package com.omarkarimli.myecommerceapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.omarkarimli.myecommerceapp.domain.models.ProductModel

@Database(
    entities = [ProductModel::class],
    version = 1,
    exportSchema = false
)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}