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

                val customerMessage = messages.firstOrNull { it.agentId == null }

                val threadTitleId = customerMessage?.userId ?: messages.first().userId

                Thread(
                    threadId = threadId,
                    customerId = threadTitleId,
                    latestMessageBody = latest.body,
                    latestMessageTimestamp = latest.timestamp
                )
            }
            .sortedByDescending { it.latestMessageTimestamp }
    }
}