package com.branchInternational.BranchAgentMessenger.ui.threads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- FIX 1: Add this import ---
import com.branchInternational.BranchAgentMessenger.domain.usecase.GetMessageThreadsUseCase

@HiltViewModel
class ThreadsViewModel @Inject constructor(
    private val getMessageThreadsUseCase: GetMessageThreadsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ThreadsUiState>(ThreadsUiState.Loading)
    val uiState: StateFlow<ThreadsUiState> = _uiState.asStateFlow()

//    init {
//        fetchThreads()
//    }

    fun fetchThreads() {
        viewModelScope.launch {
            _uiState.value = ThreadsUiState.Loading
            try {
                // --- FIX 2: Add parentheses () to call the UseCase ---
                val threads = getMessageThreadsUseCase()
                _uiState.value = ThreadsUiState.Success(threads)
            } catch (e: Exception) {
                _uiState.value = ThreadsUiState.Error("Failed to load messages: ${e.localizedMessage}")
            }
        }
    }
}