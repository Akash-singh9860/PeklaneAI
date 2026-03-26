package com.app.peklanehub.presentation.screens.summary


import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.peklanehub.domain.usecase.GeneratePdfSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val generatePdfSummaryUseCase: GeneratePdfSummaryUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow<SummaryState>(SummaryState.Idle)
    val uiState: StateFlow<SummaryState> = _uiState.asStateFlow()

    private val _navigateToSummary = MutableSharedFlow<Unit>(replay = 0)
    val navigateToSummary = _navigateToSummary.asSharedFlow()

    /**
     * Called by the UI when the user selects a PDF from their device.
     */
    fun onPdfSelected(uri: Uri?) {
        if (uri == null) {
            _uiState.value = SummaryState.Error("No file was selected.")
            return
        }
        _uiState.value = SummaryState.Loading
        viewModelScope.launch {
            _navigateToSummary.emit(Unit)
            val fileName = getFileName(uri) ?: "Shared Document"
            generatePdfSummaryUseCase(uri, fileName).fold(
                onSuccess = { finalSummary ->
                    _uiState.value = SummaryState.Success(finalSummary)
                },
                onFailure = { exception ->
                    val errorMessage = exception.message ?: "An unknown error occurred."
                    _uiState.value = SummaryState.Error(errorMessage)
                }
            )
        }
    }
    /**
     * Allows the user to clear an error or dismiss the summary to pick a new file.
     */
    fun resetState() {
        _uiState.value = SummaryState.Idle
    }

    /**
     * Helper to extract the actual filename from a Content URI.
     * Shared files often use obfuscated URIs, so we query the system for the 'DISPLAY_NAME'.
     */
    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val index = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (index != -1) {
                        result = it.getString(index)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/') ?: -1
            if (cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result
    }
}