package com.app.peklanehub.di

import android.content.Context
import com.app.peklanehub.data.local.PdfRepositoryImpl
import com.app.peklanehub.data.ml.SummarizerRepositoryImpl
import com.app.peklanehub.domain.repository.PdfRepository
import com.app.peklanehub.domain.repository.SummarizerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePdfRepository(
        @ApplicationContext context: Context
    ): PdfRepository {
        return PdfRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideSummarizerRepository(
        @ApplicationContext context: Context
    ): SummarizerRepository {
        return SummarizerRepositoryImpl(context)
    }
}