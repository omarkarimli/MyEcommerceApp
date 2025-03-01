package com.omarkarimli.myecommerceapp.di

import com.omarkarimli.myecommerceapp.data.repository.MyEcommerceRepositoryImpl
import com.omarkarimli.myecommerceapp.domain.repository.MyEcommerceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepositoryModule(
        repositoryImpl: MyEcommerceRepositoryImpl
    ): MyEcommerceRepository
}