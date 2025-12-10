package com.branchInternational.BranchAgentMessenger.data.repository

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.branchInternational.BranchAgentMessenger.data.local.dao.MessageDao
import com.branchInternational.BranchAgentMessenger.data.local.entity.MessageEntity
import com.branchInternational.BranchAgentMessenger.data.local.entity.toDomain
import com.branchInternational.BranchAgentMessenger.data.remote.BranchApiService
import com.branchInternational.BranchAgentMessenger.data.remote.dto.SendMessageRequestDto
import com.branchInternational.BranchAgentMessenger.data.remote.dto.toDomain
import com.branchInternational.BranchAgentMessenger.data.worker.SendMessageWorker
import com.branchInternational.BranchAgentMessenger.domain.model.Message
import com.branchInternational.BranchAgentMessenger.domain.repository.MessageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

class MessageRepositoryImpl @Inject constructor(
    private val api: BranchApiService,
    private val dao: MessageDao,
    @ApplicationContext private val context: Context
) : MessageRepository {

    override suspend fun getMessages(): List<Message> {
        return try {
            // 1. Get Pending messages from local DB
            val currentMessages = dao.getAllMessages()
            val pendingMessages = currentMessages.filter { it.id < 0 }

            // 2. Fetch fresh data from API
            val remoteDtos = api.getMessages()
            val serverEntities = remoteDtos.map { dto ->
                MessageEntity(
                    id = dto.id,
                    threadId = dto.threadId,
                    userId = dto.userId,
                    agentId = dto.agentId,
                    body = dto.body,
                    timestamp = dto.timestamp
                )
            }

            // 3. Clear old cache
            dao.clearAllMessages()

            // 4. --- FIX: SMART MERGE ---
            // Only keep a pending message if it is NOT yet in the server list.
            // We check if (ThreadID matches) AND (Body matches).
            val pendingMessagesToKeep = pendingMessages.filter { pending ->
                val alreadyOnServer = serverEntities.any { serverMsg ->
                    serverMsg.threadId == pending.threadId && serverMsg.body == pending.body
                }
                !alreadyOnServer // Keep it only if it's NOT on the server yet
            }

            val allMessagesToSave = serverEntities + pendingMessagesToKeep
            dao.insertMessages(allMessagesToSave)

            // 5. Return Combined List
            allMessagesToSave.map { it.toDomain() }

        } catch (e: Exception) {
            // Offline fallback
            val localMessages = dao.getAllMessages()
            if (localMessages.isNotEmpty()) {
                localMessages.map { it.toDomain() }
            } else {
                throw e
            }
        }
    }

    override suspend fun sendMessage(threadId: Int, body: String): Message {
        // ... (Keep your existing sendMessage code exactly as it is) ...
        // ... (It is correct because it inserts the negative ID message) ...

        // --- COPY-PASTE your existing sendMessage logic here ---
        val inputData = workDataOf("thread_id" to threadId, "body" to body)
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val sendRequest = OneTimeWorkRequestBuilder<SendMessageWorker>()
            .setInputData(inputData)
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
            .addTag("sending_message") // <--- ADD THIS LINE
            .build()
        WorkManager.getInstance(context).enqueue(sendRequest)

        val pendingId = -Random.nextInt(1, 100000)
        val timestampNow =
            java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.US)
                .format(java.util.Date())

        val pendingEntity = MessageEntity(
            id = pendingId,
            threadId = threadId,
            userId = "Me",
            agentId = "pending",
            body = body,
            timestamp = timestampNow
        )

        dao.insertMessages(listOf(pendingEntity))

        return Message(
            id = pendingId,
            threadId = threadId,
            userId = "Me",
            agentId = "pending",
            body = body,
            timestamp = timestampNow
        )
    }
}