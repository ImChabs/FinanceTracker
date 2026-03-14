package com.example.newfinancetracker.feature.recurring.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecurringEntryDao {
    @Query(
        """
        SELECT * FROM recurring_entries
        ORDER BY is_active DESC, next_payment_date ASC, name ASC
        """
    )
    fun observeRecurringEntries(): Flow<List<RecurringEntryEntity>>

    @Query("SELECT * FROM recurring_entries WHERE id = :entryId")
    suspend fun getRecurringEntryById(entryId: Long): RecurringEntryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRecurringEntry(entry: RecurringEntryEntity): Long

    @Query("DELETE FROM recurring_entries WHERE id = :entryId")
    suspend fun deleteRecurringEntryById(entryId: Long)
}
