package com.deepak.nexus.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/") // Replace with your actual base URL
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}


/*
*
*
As per Dagger Hilt best practices for binding an interface to its implementation, the correct and preferred way is to use @Binds.

@Provides (Use only when @Binds is not possible)
When to use: Use @Provides when:
The class you want to provide doesn't have an @Inject annotated constructor (e.g., it's a third-party library class).
You need to perform complex initialization logic before returning the instance (e.g., using a builder pattern, conditional logic).
You are providing an instance of a concrete class directly, not an interface.
Why it's less preferred for this scenario: For simple interface-to-implementation bindings where the implementation can be constructed by Hilt, @Provides involves more boilerplate and generates slightly more code, as it requires Hilt to call a method that instantiates the object.
Implementation: Requires a concrete object or class module and a concrete fun method.
*
* */