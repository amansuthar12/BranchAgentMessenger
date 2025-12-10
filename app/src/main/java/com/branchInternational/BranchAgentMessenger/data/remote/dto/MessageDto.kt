package com.branchInternational.BranchAgentMessenger.data.remote.dto

import com.branchInternational.BranchAgentMessenger.domain.model.Message
import com.google.gson.annotations.SerializedName

data class MessageDto(
    @SerializedName("id") val id: Int,
    @SerializedName("thread_id") val threadId: Int,
    @SerializedName("user_id") val userId: String,
    @SerializedName("agent_id") val agentId: String?, // Nullable: null if sent by customer
    @SerializedName("body") val body: String,
    @SerializedName("timestamp") val timestamp: String
)

// Extension function to convert DTO to Domain Model
fun MessageDto.toDomain(): Message {
    return Message(
        id = id,
        threadId = threadId,
        userId = userId,
        agentId = agentId,
        body = body,
        timestamp = timestamp
    )
}