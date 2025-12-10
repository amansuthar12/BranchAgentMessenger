package com.branchInternational.BranchAgentMessenger.domain.model

data class Thread(
    val threadId: Int,
    val customerId: String,
    val latestMessageBody: String,
    val latestMessageTimestamp: String
)