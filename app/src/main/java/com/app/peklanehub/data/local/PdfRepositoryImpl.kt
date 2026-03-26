package com.app.peklanehub.data.local

import android.content.Context
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.core.graphics.createBitmap
import com.app.peklanehub.domain.repository.PdfRepository
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PdfRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PdfRepository {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    override suspend fun extractText(uri: Uri): Result<String> = withContext(Dispatchers.IO) {
        try {
            val extractedText = tryExtractDigitalText(uri)
            if (extractedText.isNotBlank()) {
                return@withContext Result.success(extractedText)
            }
            val ocrText = extractTextViaOCR(uri)
            if (ocrText.isNotBlank()) {
                Result.success(ocrText)
            } else {
                Result.failure(Exception("Could not extract any text or images from this PDF."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun tryExtractDigitalText(uri: Uri): String {
        var document: PDDocument? = null
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                document = PDDocument.load(inputStream)
                val stripper = PDFTextStripper()
                stripper.getText(document).trim()
            } ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        } finally {
            document?.close()
        }
    }

    private suspend fun extractTextViaOCR(uri: Uri): String {
        val stringBuilder = StringBuilder()
        var fileDescriptor: ParcelFileDescriptor? = null
        var pdfRenderer: PdfRenderer? = null
        try {
            fileDescriptor = context.contentResolver.openFileDescriptor(uri, "r") ?: return ""
            pdfRenderer = PdfRenderer(fileDescriptor)
            val pageCount = pdfRenderer.pageCount
            for (i in 0 until pageCount) {
                val page = pdfRenderer.openPage(i)
                val bitmap = createBitmap(page.width * 2, page.height * 2)
                bitmap.eraseColor(Color.WHITE)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                val image = InputImage.fromBitmap(bitmap, 0)
                val result = recognizer.process(image).await()
                stringBuilder.append(result.text).append("\n\n")
                page.close()
                bitmap.recycle()
            }
        } finally {
            pdfRenderer?.close()
            fileDescriptor?.close()
        }

        return stringBuilder.toString().trim()
    }
}