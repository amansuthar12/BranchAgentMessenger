package com.branchInternational.BranchAgentMessenger.data.repository

import com.branchInternational.BranchAgentMessenger.core.utils.TokenManager
import com.branchInternational.BranchAgentMessenger.data.remote.BranchApiService
import com.branchInternational.BranchAgentMessenger.data.remote.dto.LoginRequestDto
import com.branchInternational.BranchAgentMessenger.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: BranchApiService,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            val request = LoginRequestDto(username = email, password = password)
            val response = api.login(request)

            // Save the token immediately upon success [cite: 41]
            tokenManager.saveToken(response.authToken)

            Result.success(Unit)
        } catch (e: Exception) {
            // In a real app, parse the error body for specific messages
            Result.failure(e)
        }
    }
}