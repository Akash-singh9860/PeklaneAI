package com.app.peklanehub

import android.app.ComponentCaller
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.app.peklanehub.presentation.screens.AppNavigation
import com.app.peklanehub.presentation.screens.summary.SummaryScreen
import com.app.peklanehub.presentation.screens.summary.SummaryViewModel
import com.app.peklanehub.presentation.theme.PeklaneHubTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: SummaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleSharedPdf(intent)
        enableEdgeToEdge()
        setContent {
            PeklaneHubTheme {
                AppNavigation(viewModel)
            }
        }

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleSharedPdf(intent)
    }

    private fun handleSharedPdf(intent: Intent) {
        if (intent.action == Intent.ACTION_SEND && intent.type == "application/pdf") {
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
            }
            uri?.let {
                Timber.tag("PdfPipeline").d("Shared URI received: $it")
                viewModel.onPdfSelected(it)
            }
        }
    }
}