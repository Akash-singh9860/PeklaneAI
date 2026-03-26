package com.app.peklanehub.domain.usecase

import android.net.Uri
import com.app.peklanehub.data.local.dao.SummaryDao
import com.app.peklanehub.data.local.entity.SummaryEntity
import com.app.peklanehub.domain.repository.PdfRepository
import com.app.peklanehub.domain.repository.SummarizerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class GeneratePdfSummaryUseCase @Inject constructor(
    private val pdfRepository: PdfRepository,
    private val summarizerRepository: SummarizerRepository,
    private val chunkDocumentUseCase: ChunkDocumentUseCase,
    private val summaryDao: SummaryDao
) {
    private val TAG = "PdfPipeline"

    suspend operator fun invoke(uri: Uri, fileName: String): Result<String> = withContext(Dispatchers.Default) {
        runCatching {
            Timber.tag(TAG).d("--- STARTING PDF PIPELINE ---")
            Timber.tag(TAG).d("1. Extracting text from URI...")
            val rawText = pdfRepository.extractText(uri).getOrThrow()
            Timber.tag(TAG).d("Extraction Complete. Total characters: ${rawText.length}")
            if (rawText.isBlank()) {
                throw IllegalStateException("The PDF appears to be empty or consists only of images.")
            }
            Timber.tag(TAG).d("2. Checking Nano model status...")
            val isModelReady = summarizerRepository.isModelReady()
            if (!isModelReady) {
                throw IllegalStateException("AI Model is currently downloading or unavailable.")
            }
            Timber.tag(TAG).d("3. Chunking document...")
            val chunks = chunkDocumentUseCase(rawText)
            Timber.tag(TAG).d("Chunking Complete. Total chunks created: ${chunks.size}")
            Timber.tag(TAG).d("4. Generating Smart Title...")
            val firstChunk = chunks.firstOrNull() ?: ""
            val aiTitle = if (firstChunk.isNotBlank()) {
                summarizerRepository.generateTitle(firstChunk).getOrDefault(fileName)
            } else {
                fileName
            }
            Timber.tag(TAG).d("AI Title Generated: $aiTitle")
            val finalSummaryBuilder = StringBuilder()
            for ((index, chunk) in chunks.withIndex()) {
                Timber.tag(TAG).d("5. Sending Chunk ${index + 1} of ${chunks.size} to NPU.")
                val chunkSummary = summarizerRepository.summarize(chunk).getOrThrow()
                Timber.tag(TAG).d("Chunk ${index + 1} Summarized Successfully!")
                if (chunks.size > 1) {
                    finalSummaryBuilder.append("### Part ${index + 1}\n")
                }
                finalSummaryBuilder.append(chunkSummary.trim())
                finalSummaryBuilder.append("\n\n")
            }
            val finalSummary = finalSummaryBuilder.toString().trim()
            Timber.tag(TAG).d("6. Saving to Offline History...")
            val historyEntity = SummaryEntity(
                fileName = aiTitle,
                summaryText = finalSummary
            )
            summaryDao.insertSummary(historyEntity)
            Timber.tag(TAG).d("Successfully saved to Database!")
            Timber.tag(TAG).d("--- PIPELINE COMPLETE ---")
            finalSummary
        }.onFailure { exception ->
            Timber.tag(TAG).e(exception, "PIPELINE FAILED: ${exception.message}")
        }
    }
}