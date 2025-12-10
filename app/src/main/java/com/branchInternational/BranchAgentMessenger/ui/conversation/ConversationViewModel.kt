package com.branchInternational.BranchAgentMessenger.ui.conversation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.branchInternational.BranchAgentMessenger.domain.model.Message
import com.branchInternational.BranchAgentMessenger.domain.usecase.GetConversationUseCase
import com.branchInternational.BranchAgentMessenger.domain.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val getConversationUseCase: GetConversationUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val workManager: WorkManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val threadId: Int = checkNotNull(savedStateHandle["threadId"]).toString().toInt()

    var messages by mutableStateOf<List<Message>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
    var replyText by mutableStateOf("")

    private val _uiEvent = Channel<String>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadMessages()
        observeMessageStatus()
    }

    private fun observeMessageStatus() {
        viewModelScope.launch {
            workManager.getWorkInfosByTagFlow("sending_message").collect { workInfoList ->
                if (workInfoList.any { it.state == WorkInfo.State.SUCCEEDED }) {
                    refreshMessages()
                    workManager.pruneWork()
                }
            }
        }
    }

    fun refreshMessages() {
        loadMessages()
    }

    fun loadMessages() {
        viewModelScope.launch {
            isLoading = true
            try {
                messages = getConversationUseCase(threadId)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun sendMessage() {
        if (replyText.isBlank()) return

        val textToSend = replyText

        viewModelScope.launch {
            try {
                // Attempt to send
                val newMessage = sendMessageUseCase(threadId, textToSend)

                // SUCCESS: Update list and clear the input field
                messages = messages + newMessage
                replyText = ""
            } catch (e: Exception) {
                // FAILURE: Do NOT clear replyText. User can try again.
                // Show professional message
                _uiEvent.send("No internet connection. Please check your settings and try again.")
            }
        }
    }
}