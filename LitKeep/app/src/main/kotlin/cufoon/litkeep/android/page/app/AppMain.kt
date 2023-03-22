package cufoon.litkeep.android.page.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cufoon.litkeep.android.R
import cufoon.litkeep.android.component.TabItem
import cufoon.litkeep.android.store.MainViewModel
import cufoon.litkeep.android.theme.ColorMainPinkLight

@Composable
fun AppMain() {
    val viewModel: MainViewModel = viewModel()

    val onBottomBarSelected = { index: Int ->
        viewModel.bottomBarNowAt = index
    }

    Surface {
        Column(
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars),
            Arrangement.SpaceBetween
        ) {
            Row(Modifier.weight(1f)) {
                when (viewModel.bottomBarNowAt) {
                    0 -> HomePage()
                    1 -> DataPage()
                    else -> MinePage()
                }
            }
            BottomBar(viewModel.bottomBarNowAt, onBottomBarSelected)
        }
    }
}

@Composable
fun BottomBar(nowAt: Int, whenSelect: (Int) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color(255, 255, 255, 255))
            .windowInsetsPadding(WindowInsets.navigationBars)
            .height(52.dp),
        Arrangement.SpaceBetween
    ) {
        TabItem(
            Modifier
                .weight(1f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(true, 26.dp, ColorMainPinkLight)
                ) { whenSelect(0) },
            "首页",
            R.drawable.bottom_tab_home,
            R.drawable.bottom_tab_home_on,
            nowAt == 0
        )
        TabItem(
            Modifier
                .weight(1f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(true, 26.dp, ColorMainPinkLight)
                ) { whenSelect(1) },
            "统计",
            R.drawable.bottom_tab_data,
            R.drawable.bottom_tab_data_on,
            nowAt == 1
        )
        TabItem(
            Modifier
                .weight(1f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(true, 26.dp, ColorMainPinkLight)
                ) { whenSelect(2) },
            "我的",
            R.drawable.bottom_tab_mine,
            R.drawable.bottom_tab_mine_on,
            nowAt == 2
        )
    }
}
