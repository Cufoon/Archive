package cufoon.litkeep.android.page.kind

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode
import cufoon.litkeep.android.component.Title
import cufoon.litkeep.android.service.BillKind
import cufoon.litkeep.android.service.BillKindParent
import cufoon.litkeep.android.service.BillKindService
import cufoon.litkeep.android.theme.CurveCornerShape
import cufoon.litkeep.android.util.ifNotNullOrElse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun KindList(dataList: List<BillKind>?, onItemClick: (BillKind) -> Unit) {
    Column {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 1.dp),
            mainAxisSize = SizeMode.Expand,
            mainAxisAlignment = FlowMainAxisAlignment.SpaceEvenly,
            mainAxisSpacing = 5.dp,
            crossAxisAlignment = FlowCrossAxisAlignment.Center,
            crossAxisSpacing = 5.dp
        ) {
            dataList.ifNotNullOrElse({ child ->
                child.forEach { item ->
                    key(item.KindID) {
                        Column(
                            Modifier
                                .shadow(
                                    2.dp, CurveCornerShape(20.dp), true, Color(0x00000008)
                                )
                                .clickable {
                                    onItemClick(item)
                                }
                                .width(120.dp)
                                .background(Color(255, 255, 255))
                                .padding(16.dp),
                            Arrangement.Center,
                            Alignment.CenterHorizontally) {
                            Text(text = item.Name)
                        }
                    }
                }
            }) {
                Text(text = "没有子分类")
            }
        }
    }
}

@Composable
fun KindListMain(onItemClick: (BillKind) -> Unit) {
    val scrollState = rememberScrollState()
    val (kindList, setKindList) = remember { mutableStateOf<List<BillKindParent>?>(null) }

    suspend fun queryKinds(time: Int, setValue: (List<BillKindParent>?) -> Unit) {
        val (err, data) = BillKindService.query()
        err.ifNotNullOrElse({
            Log.d("lit", it.info)
        }) {
            data?.let {
                Log.d("lit", "query $time completed")
                if (it.kind.isNotEmpty()) {
                    setValue(it.kind)
                }
            }
        }
    }

    LaunchedEffect(true) {
        withContext(Dispatchers.IO) {
            queryKinds(2, setKindList)
        }
    }

    Log.d("sss", "kindlist rerender")
    Column(Modifier.verticalScroll(scrollState)) {
        kindList.ifNotNullOrElse({
            it.forEach { item ->
                key(item.KindID) {
                    Row {
                        Title(Modifier.padding(vertical = 20.dp), item.Name)
                    }
                    KindList(item.Children) { lit ->
                        onItemClick(lit)
                    }
                }
            }
        }) {
            Text(text = "没有分类")
        }
    }
}
