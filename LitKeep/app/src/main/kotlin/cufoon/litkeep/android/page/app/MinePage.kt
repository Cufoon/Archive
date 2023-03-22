package cufoon.litkeep.android.page.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cufoon.litkeep.android.R
import cufoon.litkeep.android.component.DialogOption
import cufoon.litkeep.android.component.TopBar
import cufoon.litkeep.android.component.rememberGlobalDialogController
import cufoon.litkeep.android.store.MainViewModel
import cufoon.litkeep.android.theme.CurveCornerShape
import cufoon.litkeep.android.util.MMKV

@Composable
fun MinePage() {
    val viewModel: MainViewModel = viewModel()
    val navigator = rememberAppNavController()
    val globalDialogController = rememberGlobalDialogController()
    Column {
        TopBar("我的") {}
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .padding(18.dp, 4.dp)
                .shadow(
                    25.dp, ambientColor = Color(0x20000000), spotColor = Color(0x10000000)
                )
                .clip(CurveCornerShape(20.dp))
                .background(Color(0xFFFFFFFF))
                .padding(15.dp)
        ) {
            Column {
                Box(Modifier.padding(end = 20.dp), contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(R.drawable.user_icon),
                        contentDescription = "头像",
                        Modifier
                            .border(1.dp, Color(0x30A0A0A0), CircleShape)
                            .clip(CircleShape)
                            .alpha(0.5f)
                            .blur(10.dp)
                            .size(82.dp),
                        contentScale = ContentScale.Crop
                    )
                    Image(
                        painter = painterResource(R.drawable.user_icon),
                        contentDescription = "头像",
                        Modifier
                            .shadow(
                                25.dp,
                                ambientColor = Color(0x20000000),
                                spotColor = Color(0x10000000)
                            )
                            .clip(CircleShape)
                            .size(72.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Column {
                Text(text = "测试用户名")
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .padding(18.dp, 4.dp)
                .shadow(
                    25.dp, ambientColor = Color(0x20000000), spotColor = Color(0x10000000)
                )
                .clip(CurveCornerShape(20.dp))
                .background(Color(0xFFFFFFFF))
                .padding(6.dp)
        ) {
            Column {
                Option("账户管理", icon = R.drawable.bottom_tab_mine, onClick = {
                    viewModel.bottomBarNowAt = 0
                })
                Divider()
                Option("分类管理", icon = R.drawable.bottom_tab_mine, onClick = {
                    navigator {
                        navigate("record_kind_manage")
                    }
                })
                Divider()
                Option("记录管理", icon = R.drawable.bottom_tab_mine, onClick = {
                    viewModel.bottomBarNowAt = 0
                })
                Divider()
                Option("关于LitKeep", icon = R.drawable.bottom_tab_mine, onClick = {
                    viewModel.bottomBarNowAt = 0
                })
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 24.dp), horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "注销登录", color = Color(0x80DD0000), modifier = Modifier.clickable(
                MutableInteractionSource(), null
            ) {
                globalDialogController?.openDialog(DialogOption(onConfirm = {
                    MMKV.token = ""
                    navigator {
                        navigate("login") {
                            popUpTo("index") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                })) {
                    Text("确定要退出吗？")
                }
            })
        }
    }
}

@Composable
private fun Divider() {
    Row(
        Modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(horizontal = 50.dp)
            .background(Color(0xFFF0F0F0))
    ) {}
}

@Composable
private fun Option(
    name: String, icon: Int? = null, onClick: (() -> Unit)? = null, hasArrow: Boolean = true
) {
    Row(
        Modifier
            .clip(CurveCornerShape(20.dp))
            .clickable {
                onClick?.invoke()
            }
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            icon?.let {
                Image(
                    painter = painterResource(it),
                    contentDescription = "option_icon",
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(text = name, fontSize = 17.sp)
        }
        if (hasArrow) {
            Image(
                painter = painterResource(id = R.drawable.config_option_arrow),
                contentDescription = "option_arrow",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
