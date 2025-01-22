package com.omarkarimli.myecommerceapp.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.omarkarimli.myecommerceapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOkhttp(): OkHttpClient {
        return OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
            .callTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS).build()
    }

    /*@Singleton
    @Provides
    fun provideService(okHttpClient: OkHttpClient): ProductService {
        val retrofit =
            Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

        return retrofit.create(ProductService::class.java)
    }*/



    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE)
    }


    // Firebase
    @Singleton
    @Provides
    fun provideFirestore(): FirebaseFirestore { return FirebaseFirestore.getInstance() }

    @Singleton
    @Provides
    fun provideAuth(): FirebaseAuth { return FirebaseAuth.getInstance() }

    @Singleton
    @Provides
    fun provideCurrentUser(): FirebaseUser? { return provideAuth().currentUser }

    @Singleton
    @Provides
    fun provideUserId(): String? { return provideCurrentUser()?.uid }
}