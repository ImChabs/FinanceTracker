package com.example.newfinancetracker.feature.recurring.domain.repository

import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntry
import kotlinx.coroutines.flow.Flow

interface RecurringEntryRepository {
    fun observeRecurringEntries(): Flow<List<RecurringEntry>>

    suspend fun getRecurringEntry(entryId: Long): RecurringEntry?

    suspend fun upsertRecurringEntry(entry: RecurringEntry): Long

    suspend fun deleteRecurringEntry(entryId: Long)
}
