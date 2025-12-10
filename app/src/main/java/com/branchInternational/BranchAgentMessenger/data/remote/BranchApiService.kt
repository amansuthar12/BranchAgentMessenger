package com.branchInternational.BranchAgentMessenger.data.remote

import com.branchInternational.BranchAgentMessenger.data.remote.dto.LoginRequestDto
import com.branchInternational.BranchAgentMessenger.data.remote.dto.LoginResponseDto
import com.branchInternational.BranchAgentMessenger.data.remote.dto.MessageDto
import com.branchInternational.BranchAgentMessenger.data.remote.dto.SendMessageRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BranchApiService {

    @POST("api/login")
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto

    @GET("api/messages")
    suspend fun getMessages(): List<MessageDto>

    @POST("api/messages")
    suspend fun sendMessage(@Body request: SendMessageRequestDto): MessageDto

    @POST("api/reset")
    suspend fun resetMessages()
}