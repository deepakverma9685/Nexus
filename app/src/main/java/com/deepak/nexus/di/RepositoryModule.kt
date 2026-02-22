package com.deepak.nexus.di

import com.deepak.nexus.data.repository.UserRepositoryImpl
import com.deepak.nexus.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepository(
        impl: UserRepositoryImpl
    ): UserRepository
}