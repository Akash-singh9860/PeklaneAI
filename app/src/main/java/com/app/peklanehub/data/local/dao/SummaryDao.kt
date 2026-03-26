package com.app.peklanehub.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.peklanehub.data.local.entity.SummaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SummaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSummary(summary: SummaryEntity)
    @Query("SELECT * FROM summary_history ORDER BY timestamp DESC")
    fun getAllSummaries(): Flow<List<SummaryEntity>>
    @Delete
    suspend fun deleteSummary(summary: SummaryEntity)
    @Query("DELETE FROM summary_history")
    suspend fun clearHistory()
}