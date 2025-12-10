package com.branchInternational.BranchAgentMessenger.domain.usecase

import com.branchInternational.BranchAgentMessenger.domain.model.Message
import com.branchInternational.BranchAgentMessenger.domain.repository.MessageRepository
import javax.inject.Inject

class GetConversationUseCase @Inject constructor(
    private val repository: MessageRepository
) {
    suspend operator fun invoke(threadId: Int): List<Message> {
        // Fetch all messages, filter by thread ID, and sort by timestamp
        return repository.getMessages()
            .filter { it.threadId == threadId }
            .sortedBy { it.timestamp }
    }
}