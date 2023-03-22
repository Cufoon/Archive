package cufoon.litkeep.android.page.kind

import android.util.Log
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cufoon.litkeep.android.component.Dialog
import cufoon.litkeep.android.component.DialogOption
import cufoon.litkeep.android.component.Page
import cufoon.litkeep.android.component.TopBarWithoutContent
import cufoon.litkeep.android.theme.CurveCornerShape


@Composable
fun ManageBillKindPage() {
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val (input1, setInput) = remember { mutableStateOf("") }

    val input1provider = {
        input1
    }

    Page {
        Box {
            Column {
                TopBarWithoutContent("账单分类管理")
                KindListMain {
                    setInput(it.Name)
                    setShowDialog(true)
                }
            }
            Dialog(visible = { showDialog }, optionsProvider = {
                DialogOption(title = "修改分类名称",
                    onCancel = { setShowDialog(false) },
                    onConfirm = { Log.d("lit", "修改成功") })
            }) {
                OutlinedTextField(
                    value = input1provider(),
                    onValueChange = { setInput(it) },
                    singleLine = true,
                    shape = CurveCornerShape(32.dp),
                    modifier = Modifier
                        .focusable()
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp)
                        .padding(top = 28.dp)
                )
            }
        }
    }
}
