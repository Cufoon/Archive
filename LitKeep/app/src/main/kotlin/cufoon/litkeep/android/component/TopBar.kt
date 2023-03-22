package cufoon.litkeep.android.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopBar(
    title: String = "", content: @Composable () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .requiredHeight(48.dp)
            .padding(horizontal = 10.dp),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {
        Text(title, fontSize = 24.sp, color = Color(0xFF000000))
        content()
    }
}

@Composable
fun TopBarWithoutContent(title: String = "") {
    Row(
        Modifier
            .fillMaxWidth()
            .requiredHeight(48.dp)
            .padding(horizontal = 10.dp),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {
        Text(title, fontSize = 24.sp, color = Color(0xFF000000))
    }
}
