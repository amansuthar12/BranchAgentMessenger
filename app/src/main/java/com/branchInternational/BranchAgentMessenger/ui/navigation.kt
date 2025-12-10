package com.branchInternational.BranchAgentMessenger.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.branchInternational.BranchAgentMessenger.ui.login.LoginScreen
import com.branchInternational.BranchAgentMessenger.ui.threads.ThreadsScreen
import com.branchInternational.BranchAgentMessenger.ui.conversation.ConversationScreen

@Composable
fun AppNavGraph(startDestination: String = "login") {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("threads") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("threads") {
            ThreadsScreen(
                onThreadSelected = { threadId ->
                    navController.navigate("conversation/$threadId")
                }
            )
        }

        composable("conversation/{threadId}") { backStackEntry ->
            val threadId = backStackEntry.arguments?.getString("threadId")?.toInt() ?: 0
            ConversationScreen(threadId = threadId)
        }
    }
}