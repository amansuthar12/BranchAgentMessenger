package com.branchInternational.BranchAgentMessenger.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.branchInternational.BranchAgentMessenger.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMsg by mutableStateOf<String?>(null)

    fun autoFillPassword() {
        if (email.isNotEmpty()) {
            password = email.reversed()
        }
    }

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMsg = null

            val result = loginUseCase(email, password)

            isLoading = false
            result.onSuccess {
                onSuccess()
            }.onFailure {
                errorMsg = "Login Failed: ${it.message}"
            }
        }
    }
}