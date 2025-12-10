package com.branchInternational.BranchAgentMessenger.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("auth_token") val authToken: String
)