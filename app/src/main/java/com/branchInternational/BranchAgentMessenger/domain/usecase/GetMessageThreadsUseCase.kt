package com.branchInternational.BranchAgentMessenger.domain.usecase

import com.branchInternational.BranchAgentMessenger.domain.model.Thread
import com.branchInternational.BranchAgentMessenger.domain.repository.MessageRepository
import javax.inject.Inject

class GetMessageThreadsUseCase @Inject constructor(
    private val repository: MessageRepository
) {
    suspend operator fun invoke(): List<Thread> {
        val allMessages = repository.getMessages()

        return allMessages.groupBy { it.threadId }
            .map { (threadId, messages) ->
                // 1. Get the latest message (for the preview text & time)
                val latest = messages.maxByOrNull { it.timestamp } ?: messages.first()

                // --- FIX: ALWAYS FIND THE REAL CUSTOMER ID ---
                // We look for a message where 'agentId' is NULL (meaning it came from the customer).
                // This ensures the thread is named "User 1092" even if "User Me" spoke last.
                val customerMessage = messages.firstOrNull { it.agentId == null }

                // Fallback: If for some reason there are NO customer messages, use the first available ID.
                val threadTitleId = customerMessage?.userId ?: messages.first().userId

                Thread(
                    threadId = threadId,
                    customerId = threadTitleId, // <--- Use the fixed ID here
                    latestMessageBody = latest.body,
                    latestMessageTimestamp = latest.timestamp
                )
            }
            .sortedByDescending { it.latestMessageTimestamp }
    }
}