package com.omarkarimli.myecommerceapp.di

import android.content.Context
import androidx.room.Room
import com.omarkarimli.myecommerceapp.data.source.local.ProductDao
import com.omarkarimli.myecommerceapp.data.source.local.ProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDbModule {

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context): ProductDatabase {
        return Room.databaseBuilder(context, ProductDatabase::class.java, "productDB").build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(productDatabase: ProductDatabase): ProductDao {
        return productDatabase.productDao()
    }
}