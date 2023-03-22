package cufoon.litkeep.android.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.zIndex

@Composable
fun Sticky(
    scrollState: ScrollState,
    position: Int,
    content: @Composable RowScope.() -> Unit
) {
//    val statusBarHeight = with(LocalDensity.current) {
//        WindowInsets.statusBars.asPaddingValues().calculateTopPadding().toPx().toInt()
//    }
    val alpha by animateFloatAsState(if (position >= 0 && scrollState.value >= position) 1f else 0f)

    Row(
        Modifier
            .alpha(alpha)
            .zIndex(100f), content = content
    )
}
