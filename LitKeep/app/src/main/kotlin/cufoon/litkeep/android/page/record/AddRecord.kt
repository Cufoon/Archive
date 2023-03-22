package cufoon.litkeep.android.page.record

import android.icu.text.DecimalFormat
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import cufoon.litkeep.android.R
import cufoon.litkeep.android.component.rememberDatePickerDialog
import cufoon.litkeep.android.component.rememberTimePickerDialog
import cufoon.litkeep.android.page.app.rememberAppNavController
import cufoon.litkeep.android.theme.CurveCornerShape
import cufoon.litkeep.android.theme.LightPink
import cufoon.litkeep.android.theme.White300
import cufoon.litkeep.android.util.Calculator
import java.util.*

val cKeyLabelMap = mapOf(
    "c" to "AC",
    "d" to "DEL",
    "0" to "0",
    "1" to "1",
    "2" to "2",
    "3" to "3",
    "4" to "4",
    "5" to "5",
    "6" to "6",
    "7" to "7",
    "8" to "8",
    "9" to "9",
    "." to ".",
    "(" to "(",
    ")" to ")",
    "+" to "+",
    "-" to "-",
    "*" to "×",
    "/" to "÷",
    "=" to "=",
    "y" to "✔"
)

val n147 = listOf("c", "1", "4", "7", ".")
val n258 = listOf("(", "2", "5", "8", "0")
val n369 = listOf(")", "3", "6", "9", "/")
val n000 = listOf("d", "*", "-", "+")

@Composable
fun AddRecordPage() {
    var inputLine by remember { mutableStateOf("") }
    var inputInnerLine by remember { mutableStateOf("") }
    var needCalculate by remember { mutableStateOf(false) }
    val navigator = rememberAppNavController()
    val (selectedDate, openDatePickerDialog) = rememberDatePickerDialog(Calendar.getInstance())
    val (selectedTime, openTimePickerDialog) = rememberTimePickerDialog(Calendar.getInstance())

    val calculate = lit@{
        val xxx = Calculator.exec(inputInnerLine)
        var r = ""
        xxx?.let {
            val df = DecimalFormat("#.00")
            r = df.format(it)
        }
        if (r.isEmpty()) {
            inputLine = "计算出错"
            inputInnerLine = ""
        } else {
            inputLine = r
            inputInnerLine = r
        }
        needCalculate = false
    }

    val input = lit@{ key: String ->
        if (key == "y") {
            return@lit
        }
        if (key == "=") {
            return@lit calculate()
        }
        if (key == "c") {
            inputLine = ""
            inputInnerLine = ""
            return@lit
        }
        if (key == "d") {
            if (inputInnerLine.isNotEmpty()) {
                inputLine = inputLine.substring(0, inputLine.length - 1)
                inputInnerLine = inputInnerLine.substring(0, inputInnerLine.length - 1)
            }
            return@lit
        }
        if (key in "()+-*/") {
            needCalculate = !(key == "-" && inputInnerLine.isEmpty())
        }
        if (inputInnerLine.isEmpty()) {
            inputLine = ""
        }
        inputInnerLine += key
        inputLine += cKeyLabelMap[key]
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(White300)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .requiredHeight(48.dp), contentAlignment = Alignment.CenterStart
        ) {
            Text("返回", Modifier.clickable {
                navigator {
                    navigate("index") {
                        popBackStack()
                        popBackStack()
                    }
                }
            })
            Row(Modifier.align(Alignment.Center)) {
                Switch(items = listOf("支出", "收入"), onChanged = { })
            }
        }
        FlowRow(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            mainAxisAlignment = MainAxisAlignment.SpaceAround,
            crossAxisSpacing = 10.dp
        ) {
            repeat(20) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.bottom_tab_mine),
                        contentDescription = "icon",
                        modifier = Modifier.size(24.dp)
                    )
                    Text("这是一个类别", fontSize = 14.sp)
                }
            }
        }
        Row(
            Modifier
                .height(20.dp)
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Text("日期")
            Row {
                Text("$selectedDate", Modifier.clickable { openDatePickerDialog() })
                Text(
                    "$selectedTime",
                    Modifier
                        .padding(start = 5.dp)
                        .clickable { openTimePickerDialog() })
            }
        }
        Row(
            Modifier
                .height(20.dp)
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Text("备注")
        }
        Row(
            Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(8.dp)
                .shadow(
                    10.dp, ambientColor = Color(0x20000000), spotColor = Color(0x20000000)
                )
                .clip(RoundedCornerShape(10.dp))
                .background(LightPink)
                .padding(10.dp, 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = inputLine, fontSize = 19.sp)
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp)
        ) {
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                n147.forEach {
                    cKeyLabelMap[it]?.let { it1 -> AKey(it1, it, input) }
                }
            }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                n258.forEach {
                    cKeyLabelMap[it]?.let { it1 -> AKey(it1, it, input) }
                }
            }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                n369.forEach {
                    cKeyLabelMap[it]?.let { it1 -> AKey(it1, it, input) }
                }
            }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                n000.forEach {
                    cKeyLabelMap[it]?.let { it1 -> AKey(it1, it, input) }
                }
                AKey(
                    label = if (needCalculate) "=" else "✔",
                    key = if (needCalculate) "=" else "y",
                    input
                )
            }
        }
    }
}

@Composable
private fun AKey(
    label: String,
    key: String,
    onClick: (String) -> Unit
) {
    Row(
        Modifier
            .padding(5.dp, 3.dp)
            .clickable { onClick(key) }
            .fillMaxWidth()
            .aspectRatio(1.5f)
            .shadow(
                10.dp, ambientColor = Color(0x20000000), spotColor = Color(0x20000000)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(LightPink),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, fontSize = 20.sp)
    }
}

@Composable
private fun Switch(items: List<String>, onChanged: () -> Unit, initialSelected: Int = 0) {
    val (nowAt, setNowAt) = remember { mutableStateOf(initialSelected) }
    Row(
        Modifier
            .border(1.dp, Color.Blue, CurveCornerShape(12.dp))
            .padding(3.dp)
    ) {
        items.forEachIndexed { index, item ->
            if (nowAt == index) {
                Column(
                    Modifier
                        .clip(CurveCornerShape(10.dp))
                        .clickable { setNowAt(index) }
                        .background(Color.Blue)
                        .padding(10.dp, 2.dp)) {
                    Text(item, color = Color.White)
                }
            } else {
                Column(
                    Modifier
                        .clip(CurveCornerShape(10.dp))
                        .clickable { setNowAt(index) }
                        .padding(10.dp, 2.dp)) {
                    Text(item, color = Color.Black)
                }
            }
        }
    }
}
