package com.branchInternational.BranchAgentMessenger.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.branchInternational.BranchAgentMessenger.data.remote.BranchApiService
import com.branchInternational.BranchAgentMessenger.data.remote.dto.SendMessageRequestDto
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SendMessageWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val api: BranchApiService // Injected automatically!
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        // 1. Get the data we passed in
        val threadId = inputData.getInt("thread_id", -1)
        val body = inputData.getString("body")

        if (threadId == -1 || body.isNullOrBlank()) {
            return Result.failure()
        }

        return try {
            // 2. Attempt to send
            val request = SendMessageRequestDto(threadId, body)
            api.sendMessage(request)

            // 3. Success!
            Result.success()
        } catch (e: Exception) {
            // 4. Failed (No Internet?) -> Retry automatically whenever network returns
            Result.retry()
        }
    }
}