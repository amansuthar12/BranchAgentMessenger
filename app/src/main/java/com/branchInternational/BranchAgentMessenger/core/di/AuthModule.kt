package com.branchInternational.BranchAgentMessenger.core.di

import com.branchInternational.BranchAgentMessenger.data.repository.AuthRepositoryImpl
import com.branchInternational.BranchAgentMessenger.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}