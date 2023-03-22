package cufoon.litkeep.android.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cufoon.litkeep.android.theme.CurveCornerShape
import cufoon.litkeep.android.theme.SelectedColor
import cufoon.litkeep.android.theme.White500

@Composable
fun TabItem(modifier: Modifier, info: String, icon: Int, iconOn: Int, selected: Boolean) {
    Column(
        modifier
            .fillMaxHeight()
            .clip(CurveCornerShape(12.dp)),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        Icon(
            painterResource(if (selected) iconOn else icon),
            null,
            Modifier
                .width(28.dp)
                .height(28.dp),
            if (selected) SelectedColor else White500
        )
        Text(info, fontSize = 12.sp, color = if (selected) SelectedColor else White500)
    }
}
