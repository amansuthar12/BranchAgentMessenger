package com.branchInternational.BranchAgentMessenger.core.di

import com.branchInternational.BranchAgentMessenger.data.repository.MessageRepositoryImpl
import com.branchInternational.BranchAgentMessenger.domain.repository.MessageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // --- DELETED bindAuthRepository (It is already in AuthModule) ---

    @Binds
    @Singleton
    abstract fun bindMessageRepository(
        impl: MessageRepositoryImpl
    ): MessageRepository
}