package cufoon.ddns

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cufoon.ddns.service.ModifyRecord
import cufoon.ddns.ui.theme.DdnsTheme
import cufoon.ddns.util.CurveCornerShape
import cufoon.ddns.util.WifiTool
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App(context = this)
        }
    }
}

@Composable
fun App(context: Context) {
    var loading by remember { mutableStateOf(false) }

    val onRefreshIP = {
        loading = true
        val ip = WifiTool.getSystemIP(context)
        val scope = MainScope()
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                ModifyRecord.changeARecord(ip)
            }
            val toast = Toast.makeText(context, "解析刷新失败", Toast.LENGTH_SHORT)
            if (result) {
                toast.setText("解析刷新成功")
            }
            toast.show()
            loading = false
        }
    }

    DdnsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Greeting("Android")
                RefreshARecordButton(loading = loading) {
                    Toast.makeText(context, "点击了按钮", Toast.LENGTH_SHORT).show()
                    onRefreshIP()
                }
            }
        }
    }
}

@Composable
fun RefreshARecordButton(loading: Boolean = false, loadingText: String = "加载中", click: () -> Unit) {
    Button(
        onClick = click,
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 12.dp,
            end = 20.dp,
            bottom = 12.dp
        ),
        shape = CurveCornerShape()
    ) {
        Icon(
            Icons.Filled.Favorite,
            contentDescription = "Favorite",
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text("刷新解析")
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!", modifier = Modifier.absolutePadding(top = 50.dp))
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    App(context = LocalContext.current)
}