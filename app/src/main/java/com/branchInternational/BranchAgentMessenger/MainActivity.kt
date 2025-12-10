package com.branchInternational.BranchAgentMessenger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.branchInternational.BranchAgentMessenger.core.utils.TokenManager
import com.branchInternational.BranchAgentMessenger.ui.BranchApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val startDestination = if (tokenManager.getToken() != null) "threads" else "login"

        setContent {
            BranchApp(startDestination = startDestination)
        }
    }
}