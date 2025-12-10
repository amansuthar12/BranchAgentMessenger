package com.branchInternational.BranchAgentMessenger

import com.branchInternational.BranchAgentMessenger.domain.model.Message
import com.branchInternational.BranchAgentMessenger.domain.repository.MessageRepository
import com.branchInternational.BranchAgentMessenger.domain.usecase.GetMessageThreadsUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class GetMessageThreadsUseCaseTest {

    private val repository = mock(MessageRepository::class.java)
    private val useCase = GetMessageThreadsUseCase(repository)

    @Test
    fun `should group messages by threadId and sort by latest timestamp`() = runBlocking {
        // 1. Given (Mock Data)
        val messages = listOf(
            // Thread A (Newer)
            Message(1, 22, "UserA", null, "Old msg", "2023-01-01T10:00:00.000Z"),
            Message(2, 22, "Me", "agent_1", "Newest msg", "2023-01-01T12:00:00.000Z"), // Latest in Thread A

            // Thread B (Older)
            Message(3, 99, "UserB", null, "Hello", "2023-01-01T09:00:00.000Z")
        )
        `when`(repository.getMessages()).thenReturn(messages)

        // 2. When
        val result = useCase()

        // 3. Then
        assertEquals(2, result.size) // Should have 2 threads

        // Check sorting: Thread 22 is newer, so it should be first
        assertEquals(22, result[0].threadId)
        assertEquals(99, result[1].threadId)

        // Check preview text: Should use the LATEST message ("Newest msg")
        assertEquals("Newest msg", result[0].latestMessageBody)

        // Check User Name: Should be "UserA" (Customer), NOT "Me" (Agent)
        assertEquals("UserA", result[0].customerId)
    }
}