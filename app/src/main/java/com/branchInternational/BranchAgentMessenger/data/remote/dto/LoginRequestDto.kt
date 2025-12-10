package com.branchInternational.BranchAgentMessenger.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    @SerializedName("username") val username: String, // Your email
    @SerializedName("password") val password: String  // Your email reversed
)