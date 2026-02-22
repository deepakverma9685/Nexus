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


/*
* @Binds (Recommended Best Practice)
When to use: Use @Binds when you need to provide an implementation of an interface, and the implementation's constructor can be directly injected by Hilt (i.e., it has an @Inject annotated constructor).
Why it's better:
Efficiency: @Binds is more efficient than @Provides because it tells Dagger Hilt directly how to map an interface to an implementation without needing to instantiate the implementation and return it. This results in less generated code and better performance.
Readability: It clearly expresses the intent of binding an interface to a concrete type.
Implementation: Requires an abstract class module and an abstract fun method.
*
*
*
* Conclusion
For binding UserRepository to UserRepositoryImpl, where UserRepositoryImpl likely has an
* @Inject constructor, @Binds is the clear best practice. It's more efficient, generates less
* code, and explicitly communicates the binding intent. You've already correctly implemented
* this in your RepositoryModule.kt.
*
*
* */