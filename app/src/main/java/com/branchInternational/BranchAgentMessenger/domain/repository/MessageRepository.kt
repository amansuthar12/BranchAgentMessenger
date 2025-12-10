package com.branchInternational.BranchAgentMessenger.domain.repository

import com.branchInternational.BranchAgentMessenger.domain.model.Message

interface MessageRepository {
    suspend fun getMessages(): List<Message>
    suspend fun sendMessage(threadId: Int, body: String): Message
}