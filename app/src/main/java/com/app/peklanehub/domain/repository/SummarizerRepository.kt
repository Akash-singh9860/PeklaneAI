package com.app.peklanehub.domain.repository

interface SummarizerRepository {
    suspend fun isModelReady(): Boolean
    suspend fun summarize(textChunk: String): Result<String>
    suspend fun generateTitle(textChunk: String): Result<String>
    fun close()
}