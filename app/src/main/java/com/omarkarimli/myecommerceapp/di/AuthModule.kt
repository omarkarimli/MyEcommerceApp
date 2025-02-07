package com.omarkarimli.myecommerceapp.di

import com.google.firebase.auth.FirebaseAuth
import com.omarkarimli.myecommerceapp.data.repository.AuthRepositoryImpl
import com.omarkarimli.myecommerceapp.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Singleton
    @Provides
    fun provideAuth(): FirebaseAuth { return FirebaseAuth.getInstance() }
}