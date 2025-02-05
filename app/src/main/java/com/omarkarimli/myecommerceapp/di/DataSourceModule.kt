package com.omarkarimli.myecommerceapp.di

import com.omarkarimli.myecommerceapp.data.source.local.LocalDataSource
import com.omarkarimli.myecommerceapp.data.source.local.LocalDataSourceImpl
import com.omarkarimli.myecommerceapp.data.source.remote.RemoteDataSource
import com.omarkarimli.myecommerceapp.data.source.remote.RemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindRemoteDataSource(
        remoteDataSourceImpl: RemoteDataSourceImpl
    ): RemoteDataSource

    @Binds
    @Singleton
    abstract fun bindLocalDataSource(
        localDataSourceImpl: LocalDataSourceImpl
    ): LocalDataSource
}