package com.app.peklanehub.data.ml

import android.content.Context
import com.app.peklanehub.domain.repository.SummarizerRepository
import com.google.mlkit.genai.summarization.Summarization
import com.google.mlkit.genai.summarization.Summarizer
import com.google.mlkit.genai.summarization.SummarizerOptions
import com.google.mlkit.genai.summarization.SummarizationRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.guava.await
import javax.inject.Inject

class SummarizerRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SummarizerRepository {
    private var summarizer: Summarizer? = null
    private var titleGenerator: Summarizer? = null

    init {
        val options = SummarizerOptions.builder(context)
            .setInputType(SummarizerOptions.InputType.ARTICLE)
            .setOutputType(SummarizerOptions.OutputType.THREE_BULLETS)
            .setLongInputAutoTruncationEnabled(true)
            .build()

        val titleOptions = SummarizerOptions.builder(context)
            .setInputType(SummarizerOptions.InputType.ARTICLE)
            .setOutputType(SummarizerOptions.OutputType.ONE_BULLET)
            .setLongInputAutoTruncationEnabled(true)
            .build()

        summarizer = Summarization.getClient(options)
        titleGenerator = Summarization.getClient(titleOptions)
    }

    override suspend fun isModelReady(): Boolean {
        return try {
            val status = summarizer?.checkFeatureStatus()?.await()
            status != null
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun summarize(textChunk: String): Result<String> {
        return runCatching {
            val currentSummarizer = summarizer ?: throw IllegalStateException("Summarizer not initialized")
            val request = SummarizationRequest.builder(textChunk).build()
            val result = currentSummarizer.runInference(request).await()
            val summaryText = result.summary
            if (summaryText.isBlank()) {
                throw Exception("Gemini Nano returned an empty response.")
            }
            summaryText
        }
    }

    override suspend fun generateTitle(textChunk: String): Result<String> {
        return runCatching {
            val currentGenerator = titleGenerator ?: throw IllegalStateException("Title generator not initialized")
            val request = SummarizationRequest.builder(textChunk).build()
            val result = currentGenerator.runInference(request).await()
            val rawOutput = result.summary
            if (rawOutput.isBlank()) {
                return@runCatching "Untitled Document"
            }
            rawOutput
                .replace("•", "")
                .replace("*", "")
                .replace("-", "")
                .trim()
                .take(60)
        }
    }

    override fun close() {
        summarizer?.close()
    }
}