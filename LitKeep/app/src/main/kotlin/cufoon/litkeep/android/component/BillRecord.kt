package cufoon.litkeep.android.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun Dot(color: Color) {
    Column(
        modifier = Modifier
            .size(8.dp)
            .background(color, RoundedCornerShape(100))
    ) {}
}

@Composable
fun Money(money: Double?, kind: Int, color: Color) {
    val moneyText = when (kind) {
        1 -> "-${money}"
        else -> "+${money}"
    }
    Text(text = moneyText, color = color, fontWeight = FontWeight.Bold)
}

@Composable
fun BillRecordLine(tag: String, money: Double?, kind: Int, color: Color) {
    Row(
        Modifier
            .padding(horizontal = 5.dp)
            .fillMaxWidth()
            .requiredHeight(48.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Dot(color)
            Text(tag, Modifier.padding(horizontal = 6.dp))
        }
        Row {
            Money(money = money, kind = kind, color = color)
        }
    }
}