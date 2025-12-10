package com.branchInternational.BranchAgentMessenger.ui.threads

import com.branchInternational.BranchAgentMessenger.domain.model.Thread

sealed interface ThreadsUiState {
    // 1. Loading State (No data)
    data object Loading : ThreadsUiState

    // 2. Success State (Has data)
    data class Success(val threads: List<Thread>) : ThreadsUiState

    // 3. Error State (Has error message)
    data class Error(val message: String) : ThreadsUiState
}