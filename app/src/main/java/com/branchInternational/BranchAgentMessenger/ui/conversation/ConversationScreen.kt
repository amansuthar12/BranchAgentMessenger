package com.branchInternational.BranchAgentMessenger.ui.conversation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.branchInternational.BranchAgentMessenger.core.utils.DateUtils
import com.branchInternational.BranchAgentMessenger.domain.model.Message
import com.branchInternational.BranchAgentMessenger.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen(
    threadId: Int,
    viewModel: ConversationViewModel = hiltViewModel()
) {
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    androidx.compose.runtime.DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) viewModel.refreshMessages()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { message -> snackbarHostState.showSnackbar(message) }
    }

    LaunchedEffect(viewModel.messages.size) {
        if (viewModel.messages.isNotEmpty()) listState.animateScrollToItem(viewModel.messages.size - 1)
    }

    Scaffold(
        containerColor = BranchBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Thread #$threadId", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Support Agent", style = MaterialTheme.typography.labelSmall, color = BranchTeal)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = BranchBackground)
            )
        },
        bottomBar = {
            PremiumInputBar(
                value = viewModel.replyText,
                onValueChange = { viewModel.replyText = it },
                onSend = { viewModel.sendMessage() }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (viewModel.isLoading) CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = BranchTeal)

            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(viewModel.messages) { message ->
                    PremiumChatBubble(message)
                }
            }
        }
    }
}

@Composable
fun PremiumChatBubble(message: Message) {
    val isMe = message.agentId != null
    val alignment = if (isMe) Alignment.End else Alignment.Start

    val shape = if (isMe) {
        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 4.dp)
    } else {
        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 4.dp, bottomEnd = 20.dp)
    }

    val bubbleColor = if (isMe) BranchTeal else SurfaceWhite
    val textColor = if (isMe) Color.White else TextPrimary

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = alignment) {
        Surface(
            color = bubbleColor,
            shape = shape,
            shadowElevation = 2.dp,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = message.body,
                    style = MaterialTheme.typography.bodyLarge,
                    color = textColor
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = DateUtils.formatTimeOnly(message.timestamp),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontSize = 11.sp
            )
            if (message.id < 0) {
                Spacer(modifier = Modifier.width(4.dp))
                CircularProgressIndicator(modifier = Modifier.size(10.dp), strokeWidth = 2.dp, color = BranchTeal)
            }
        }
    }
}

@Composable
fun PremiumInputBar(value: String, onValueChange: (String) -> Unit, onSend: () -> Unit) {
    Row(
        modifier = Modifier
            .background(BranchBackground)
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .shadow(2.dp, CircleShape)
                .background(SurfaceWhite, CircleShape),
            placeholder = { Text("Type a message...", color = TextSecondary) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = SurfaceWhite,
                unfocusedContainerColor = SurfaceWhite,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = CircleShape,
            maxLines = 4
        )

        Spacer(modifier = Modifier.width(12.dp))

        FloatingActionButton(
            onClick = onSend,
            containerColor = BranchTeal,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier.size(50.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
        }
    }
}