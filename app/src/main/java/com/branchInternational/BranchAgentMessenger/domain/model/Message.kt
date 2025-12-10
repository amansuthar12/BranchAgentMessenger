package com.branchInternational.BranchAgentMessenger.domain.model

data class Message(
    val id: Int,
    val threadId: Int,
    val userId: String,
    val agentId: String?,
    val body: String,
    val timestamp: String
)