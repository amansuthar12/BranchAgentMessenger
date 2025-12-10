package com.branchInternational.BranchAgentMessenger.domain.usecase

import com.branchInternational.BranchAgentMessenger.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email and password cannot be empty"))
        }
        if (password != email.reversed()) {
            return Result.failure(IllegalArgumentException("Invalid credentials. Hint: Password is email reversed."))
        }
        return repository.login(email, password)
    }
}