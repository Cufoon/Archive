package cufoon.litkeep.android.page.app

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cufoon.litkeep.android.R
import cufoon.litkeep.android.component.BillRecordLine
import cufoon.litkeep.android.component.Sticky
import cufoon.litkeep.android.component.Title
import cufoon.litkeep.android.service.BillRecord
import cufoon.litkeep.android.service.BillRecordService
import cufoon.litkeep.android.service.ReqBillRecordQuery
import cufoon.litkeep.android.theme.CurveCornerShape
import cufoon.litkeep.android.theme.Shouru
import cufoon.litkeep.android.theme.White300
import cufoon.litkeep.android.theme.Zhichu
import cufoon.litkeep.android.util.ifNotNullOrElse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime

@Composable
fun HomePage() {
    val navigator = rememberAppNavController()
    val scrollState = rememberScrollState()
    var title1position by remember { mutableStateOf(-1) }
    var title2position by remember { mutableStateOf(-1) }

    val (recordList, setRecordList) = remember {
        mutableStateOf<List<BillRecord>?>(null)
    }

    val (recordList1, setRecordList1) = remember {
        mutableStateOf<List<BillRecord>?>(null)
    }

    suspend fun queryRecentWeek(time: Int, setValue: (List<BillRecord>?) -> Unit) {
        val (err, data) = BillRecordService.query(
            ReqBillRecordQuery(
                "AJq1yOlQ",
                "2=2@2=2+2=2@2=2+",
                OffsetDateTime.now().minusDays(time.toLong()),
                OffsetDateTime.now()
            )
        )
        err.ifNotNullOrElse({
            Log.d("lit", it.info)
        }) {
            data?.let {
                setValue(it.record)
            }
        }
    }

    LaunchedEffect(true) {
        withContext(Dispatchers.Default) {
            queryRecentWeek(2, setRecordList)
            queryRecentWeek(30, setRecordList1)
        }
    }

    Box(
        Modifier.fillMaxSize()
    ) {
        Sticky(scrollState, title1position) {
            Title(
                Modifier
                    .background(White300)
                    .padding(vertical = 20.dp), "今日消费"
            )
        }
        Sticky(scrollState, title2position) {
            Title(
                Modifier
                    .background(White300)
                    .padding(vertical = 20.dp), "本周消费", Color(0xFFFADB5F)
            )
        }
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 120.dp)
        ) {
            ContentListWithTitle(recordList, {
                BillRecordLine(
                    tag = it.Mark ?: "none", money = it.Value, kind = it.Type ?: 0, color = Zhichu
                )
            }) {
                Title(
                    Modifier
                        .onGloballyPositioned {
                            title1position = it.positionInParent().y.toInt()
                        }
                        .onPlaced {
                            title1position = it.positionInParent().y.toInt()
                        }
                        .padding(vertical = 20.dp), "今日消费")
            }
            ContentListWithTitle(recordList1, {
                BillRecordLine(
                    tag = it.Mark ?: "none",
                    money = it.Value,
                    kind = it.Type ?: 0,
                    color = if (it.Type != 0) Zhichu else Shouru
                )
            }) {
                Title(
                    Modifier
                        .onGloballyPositioned {
                            title2position = it.positionInParent().y.toInt()
                        }
                        .onPlaced {
                            title2position = it.positionInParent().y.toInt()
                        }
                        .padding(vertical = 20.dp)
                        .padding(top = 10.dp), "本周消费", Color(0xFFFADB5F))
            }
        }
        Row(
            Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 30.dp, bottom = 30.dp)
        ) {
            Button(
                onClick = {
                    navigator {
                        navigate("record_add")
                    }
                },
                Modifier
                    .size(56.dp)
                    .align(Alignment.CenterVertically),
                shape = CurveCornerShape(18.dp),
                contentPadding = PaddingValues.Absolute()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.home_add_button),
                    contentDescription = "add button",
                    Modifier
                        .size(26.dp)
                        .padding(),
                    Color(0xFFFFFFFF)
                )
            }
        }
    }
}

@Composable
private fun <T> ContentListWithTitle(
    recordList: List<T>?, itemRender: @Composable (T) -> Unit, title: @Composable () -> Unit
) {
    title()
    Row(
        Modifier
            .defaultMinSize(minHeight = 200.dp)
            .fillMaxSize()
            .padding(18.dp, 4.dp)
            .shadow(
                25.dp, ambientColor = Color(0x20000000), spotColor = Color(0x10000000)
            )
            .clip(CurveCornerShape(20.dp))
            .background(Color(0xFFFFFFFF))
    ) {
        Column(Modifier.padding(12.dp)) {
            recordList.ifNotNullOrElse({ rList ->
                rList.forEach {
                    itemRender(it)
                }
            }) {
                Text(text = "暂无内容")
            }
        }
    }
}
