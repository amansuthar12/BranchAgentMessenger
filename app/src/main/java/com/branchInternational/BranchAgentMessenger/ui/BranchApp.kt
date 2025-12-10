package com.branchInternational.BranchAgentMessenger.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.branchInternational.BranchAgentMessenger.ui.navigation.AppNavGraph

@Composable
fun BranchApp(startDestination: String) { // Accept parameter
    MaterialTheme {
        Surface {
            AppNavGraph(startDestination = startDestination)
        }
    }
}