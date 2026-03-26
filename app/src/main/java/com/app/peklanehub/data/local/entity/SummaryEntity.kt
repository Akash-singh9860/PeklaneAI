package com.app.peklanehub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "summary_history")
data class SummaryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fileName: String,
    val summaryText: String,
    val timestamp: Long = System.currentTimeMillis()
)