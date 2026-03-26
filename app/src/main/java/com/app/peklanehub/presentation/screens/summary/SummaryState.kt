package com.app.peklanehub.presentation.screens.summary

sealed interface SummaryState {
    data object Idle : SummaryState
    data object Loading : SummaryState
    data class Success(val summaryText: String) : SummaryState
    data class Error(val message: String) : SummaryState
}