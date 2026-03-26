package com.app.peklanehub.domain.repository

import android.net.Uri

interface PdfRepository {
    suspend fun extractText(uri: Uri): Result<String>
}