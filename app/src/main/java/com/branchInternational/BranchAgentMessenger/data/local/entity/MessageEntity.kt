package com.branchInternational.BranchAgentMessenger.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.branchInternational.BranchAgentMessenger.domain.model.Message

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: Int,
    val threadId: Int,
    val userId: String,
    val agentId: String?,
    val body: String,
    val timestamp: String
)

// Helper to convert Entity -> Domain
fun MessageEntity.toDomain(): Message {
    return Message(id, threadId, userId, agentId, body, timestamp)
}

// Helper to convert Domain -> Entity (optional, mostly for sending)
fun Message.toEntity(): MessageEntity {
    return MessageEntity(id, threadId, userId, agentId, body, timestamp)
}