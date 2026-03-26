package com.app.peklanehub.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.peklanehub.data.local.dao.SummaryDao
import com.app.peklanehub.data.local.entity.SummaryEntity

@Database(entities = [SummaryEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun summaryDao(): SummaryDao

}