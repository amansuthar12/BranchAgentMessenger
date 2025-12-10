package com.branchInternational.BranchAgentMessenger.core.di

import com.branchInternational.BranchAgentMessenger.core.network.AuthInterceptor
import com.branchInternational.BranchAgentMessenger.data.remote.BranchApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideBranchApi(client: OkHttpClient): BranchApiService {
        return Retrofit.Builder()
            .baseUrl("https://android-messaging.branch.co/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BranchApiService::class.java)
    }
}