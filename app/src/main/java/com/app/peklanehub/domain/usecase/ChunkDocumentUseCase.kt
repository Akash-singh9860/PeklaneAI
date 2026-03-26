package com.app.peklanehub.domain.usecase

import javax.inject.Inject

class ChunkDocumentUseCase @Inject constructor() {

    /**
     * Splits a massive string into smaller, overlapping chunks safely.
     * * @param text The raw extracted PDF text.
     * @param maxCharsPerChunk Safe limit for Nano (12,000 chars ≈ 3,000 tokens).
     * @param overlapChars How much context to carry over between chunks.
     */
    operator fun invoke(
        text: String,
        maxCharsPerChunk: Int = 7500,
        overlapChars: Int = 300
    ): List<String> {

        if (text.length <= maxCharsPerChunk) {
            return listOf(text)
        }

        val chunks = mutableListOf<String>()
        var currentIndex = 0

        while (currentIndex < text.length) {
            val projectedEndIndex = currentIndex + maxCharsPerChunk
            if (projectedEndIndex >= text.length) {
                chunks.add(text.substring(currentIndex).trim())
                break
            }
            val safeEndIndex = findLogicalBreak(text, projectedEndIndex, currentIndex)
            chunks.add(text.substring(currentIndex, safeEndIndex).trim())
            currentIndex = safeEndIndex - overlapChars
            if (currentIndex <= 0 || safeEndIndex - currentIndex <= 0) {
                currentIndex = safeEndIndex
            }
        }
        return chunks
    }

    /**
     * Scans backwards from the projected cut-off point to find the nearest
     * end of a sentence or paragraph so we don't chop a word in half.
     */
    private fun findLogicalBreak(text: String, projectedEnd: Int, startIndex: Int): Int {
        val minimumCutoff = startIndex + ((projectedEnd - startIndex) / 2)
        for (i in projectedEnd downTo minimumCutoff) {
            if (text[i] == '\n' || text[i] == '.') {
                return i + 1
            }
        }
        return projectedEnd
    }
}