package com.app.peklanehub.presentation.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.peklanehub.data.local.dao.SummaryDao
import com.app.peklanehub.data.local.entity.SummaryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val summaryDao: SummaryDao
) : ViewModel() {
    val historyState: StateFlow<List<SummaryEntity>> = summaryDao.getAllSummaries()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteSummary(summary: SummaryEntity) {
        viewModelScope.launch {
            summaryDao.deleteSummary(summary)
        }
    }
}