package com.example.newfinancetracker.feature.recurring.data.repository

import com.example.newfinancetracker.feature.recurring.data.local.RecurringEntryDao
import com.example.newfinancetracker.feature.recurring.data.local.toDomain
import com.example.newfinancetracker.feature.recurring.data.local.toEntity
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntry
import com.example.newfinancetracker.feature.recurring.domain.repository.RecurringEntryRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineRecurringEntryRepository @Inject constructor(
    private val recurringEntryDao: RecurringEntryDao
) : RecurringEntryRepository {

    override fun observeRecurringEntries(): Flow<List<RecurringEntry>> =
        recurringEntryDao.observeRecurringEntries().map { entries ->
            entries.map { it.toDomain() }
        }

    override suspend fun getRecurringEntry(entryId: Long): RecurringEntry? =
        recurringEntryDao.getRecurringEntryById(entryId)?.toDomain()

    override suspend fun upsertRecurringEntry(entry: RecurringEntry): Long =
        recurringEntryDao.upsertRecurringEntry(entry.toEntity())

    override suspend fun deleteRecurringEntry(entryId: Long) {
        recurringEntryDao.deleteRecurringEntryById(entryId)
    }
}
