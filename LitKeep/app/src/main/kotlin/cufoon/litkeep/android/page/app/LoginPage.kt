package cufoon.litkeep.android.page.app

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cufoon.litkeep.android.R
import cufoon.litkeep.android.component.DialogOption
import cufoon.litkeep.android.component.rememberGlobalDialogController
import cufoon.litkeep.android.service.UserService
import cufoon.litkeep.android.theme.CurveCornerShape
import cufoon.litkeep.android.util.MMKV
import cufoon.litkeep.android.util.ifNotNullOrElse
import kotlinx.coroutines.launch

@Composable
fun LoginPage() {
    var mainContentPosition by remember { mutableStateOf(0) }
    var statusBarBackgroundColor by remember { mutableStateOf(Color(0xFF00FFFF)) }

    val setContentPosition = fun(x: Int) {
        mainContentPosition = x
    }

    LaunchedEffect(mainContentPosition) {
        statusBarBackgroundColor =
            if (mainContentPosition <= 0) Color(0xFFFFFFFF) else Color(0x00FFFFFF)
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        BackgroundPicture()
        Content(this@BoxWithConstraints.maxHeight, setContentPosition)
        StatusBarPlaceHolder(statusBarBackgroundColor)
    }
}

@Composable
fun BackgroundPicture() {
    Image(
        painterResource(id = R.drawable.login_bg),
        contentDescription = "login background picture",
        Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentScale = ContentScale.Crop
    )
    Row(
        Modifier
            .height(300.dp)
            .fillMaxWidth()
            .background(Color(0x80FFFFFF))
    ) {}
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Content(minHeight: Dp = Dp.Unspecified, setPosition: (Int) -> Unit) {
    val navigator = rememberAppNavController()
    val dialogController = rememberGlobalDialogController()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val onSubmitLogin = onSubmitLogin@{
        if (username == "" || password == "") {
            Toast.makeText(context, "请输入用户名和密码", Toast.LENGTH_SHORT).show()
            return@onSubmitLogin
        }
        keyboardController?.hide()
        focusManager.clearFocus()
//        Toast.makeText(context, "$username,$password", Toast.LENGTH_SHORT).show()
        scope.launch {
            val (err, data) = UserService.login(username, password)
            err.ifNotNullOrElse({
                dialogController?.openDialog(DialogOption(title = "登录结果", rmCancel = true)) {
                    Text(text = it.info)
                }
            }) {
                data?.let {
                    if (it.logined) {
                        MMKV.username = username
                        MMKV.password = password
                        MMKV.token = it.token
                        navigator {
                            navigate("index") {
                                popUpTo("login") { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                }
            }
        }
    }

    Box(Modifier.verticalScroll(scrollState)) {
        Column(
            Modifier
                .fillMaxWidth()
                .heightIn(min = minHeight)
                .padding(top = 200.dp)
                .onGloballyPositioned { layoutCoordinates ->
                    setPosition(layoutCoordinates.positionInRoot().y.toInt())
                }
                .shadow(
                    48.dp,
                    RoundedCornerShape(topStart = 34.dp, topEnd = 34.dp),
                    ambientColor = Color.Black,
                    spotColor = Color.Black
                )
                .clip(RoundedCornerShape(topStart = 34.dp, topEnd = 34.dp))
                .background(Color(0xFFFFFFFF))
                .padding(vertical = 37.dp, horizontal = 10.dp)) {
            Text(
                text = "登录",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )
            OutlinedTextField(value = username,
                onValueChange = { username = it },
                singleLine = true,
                placeholder = { Text("用户名/邮箱") },
                shape = CurveCornerShape(32.dp),
                modifier = Modifier
                    .focusable()
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp)
                    .padding(top = 28.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                placeholder = { Text("密码") },
                shape = CurveCornerShape(32.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp)
                    .padding(top = 28.dp)
            )
            Button(
                onClick = onSubmitLogin,
                shape = CurveCornerShape(32.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp)
                    .padding(top = 28.dp)
                    .background(
                        Color(0xFFE87D93), CurveCornerShape(32.dp)
                    )
                    .defaultMinSize(minHeight = 56.dp)
            ) {
                Text(
                    "登录",
                    Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun StatusBarPlaceHolder(bgc: Color) {
    Row(
        Modifier
            .windowInsetsTopHeight(
                WindowInsets.statusBars
            )
            .fillMaxWidth()
            .background(bgc)
    ) {}
}
