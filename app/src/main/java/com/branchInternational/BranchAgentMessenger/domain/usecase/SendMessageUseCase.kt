package com.branchInternational.BranchAgentMessenger.domain.usecase

import com.branchInternational.BranchAgentMessenger.domain.model.Message
import com.branchInternational.BranchAgentMessenger.domain.repository.MessageRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: MessageRepository
) {
    suspend operator fun invoke(threadId: Int, body: String): Message {
        return repository.sendMessage(threadId, body)
    }
}