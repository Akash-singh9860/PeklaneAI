package com.app.peklanehub.di

import android.app.Application
import androidx.room.Room
import com.app.peklanehub.data.local.AppDatabase
import com.app.peklanehub.data.local.dao.SummaryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "peklane_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideSummaryDao(db: AppDatabase): SummaryDao {
        return db.summaryDao()
    }
}