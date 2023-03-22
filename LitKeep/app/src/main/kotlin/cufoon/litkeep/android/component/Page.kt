package cufoon.litkeep.android.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun Page(content: @Composable() (ColumnScope.() -> Unit)) {
    Column(
        Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars), content = content
    )
}
