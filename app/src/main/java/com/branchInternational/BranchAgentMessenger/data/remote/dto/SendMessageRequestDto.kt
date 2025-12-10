package com.branchInternational.BranchAgentMessenger.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SendMessageRequestDto(
    @SerializedName("thread_id") val threadId: Int,
    @SerializedName("body") val body: String
)