package cufoon.gallery.android.hook

import androidx.compose.runtime.*

@Composable
fun rememberCircleCounter(initial: Int = 0, reset: Int = 0): Pair<Int, () -> Unit> {
    var count by remember { mutableStateOf(initial) }
    val update = remember(true) {
        lit@{
            count = if (count > 10000) reset else count + 1
            return@lit
        }
    }
    return Pair(count, update)
}