package com.app.peklanehub.presentation.common

import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.Markwon

@Composable
fun MarkwonText(
    markdown: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val markwon = remember(context) { Markwon.builder(context).build() }
    val themeTextColor = color.toArgb()


    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { ctx ->
            TextView(ctx).apply {
                setTextColor(themeTextColor)
                textSize = 16f
            }
        },
        update = { textView ->
            textView.setTextColor(themeTextColor)
            markwon.setMarkdown(textView, markdown)
        }
    )
}