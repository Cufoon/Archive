package cufoon.gallery.android.page

import android.Manifest
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import cufoon.gallery.android.hook.rememberCircleCounter
import cufoon.gallery.android.ui.theme.CurveCornerShape
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


data class ImageDataItem(
    val id: Long, val uri: Uri, val name: String, val size: Int, val thumbnail: ImageBitmap
)

val collection: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    MediaStore.Images.Media.getContentUri(
        MediaStore.VOLUME_EXTERNAL
    )
} else {
    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
}

val projection = arrayOf(
    MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.SIZE
)

// Show only videos that are at least 1 second in duration.
//const val selection = "${MediaStore.Images.Media.DURATION} >= ?"
//val selectionArgs = arrayOf(
//    TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS).toString()
//)

const val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainPage() {
    val (checkPermission, shouldCheckPermission) = rememberCircleCounter()
    var isPermissionReqEnd by remember { mutableStateOf(false) }
    var isRequestPermission by remember { mutableStateOf(false) }

    val storagePermissionsState = rememberMultiplePermissionsState(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) listOf(
            Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_EXTERNAL_STORAGE
        ) else listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    ) {
        isPermissionReqEnd = true
    }
    val (permissionGranted, setPermissionGranted) = remember { mutableStateOf(false) }
    val (permissionTip, setPermissionTip) = remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val (imageList, setImageList) = remember { mutableStateOf(listOf<ImageDataItem>()) }
    val (loading, setLoading) = remember { mutableStateOf(true) }
    val (loadPercent, setLoadPercent) = remember { mutableStateOf(0) }
    val (pictureNum, setPictureNum) = remember { mutableStateOf(0) }

    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val context by rememberUpdatedState(LocalContext.current)

    fun readPictures() {
        setLoading(true)
        setPictureNum(0)
        setImageList(listOf())
        coroutineScope.launch(Dispatchers.IO) {
            val imageDataItemMutableList = mutableListOf<ImageDataItem>()
            val query = context.contentResolver.query(
                collection, projection, null, null, sortOrder
            )
            query?.use { cursor ->
                val picNum = cursor.count
                setPictureNum(picNum)
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
                var processNum = 1
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val size = cursor.getInt(sizeColumn)

                    val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                    )

                    val thumbnail = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        context.contentResolver.loadThumbnail(
                            contentUri, Size(400, 400), null
                        ).asImageBitmap()
                    } else {
                        MediaStore.Images.Thumbnails.getThumbnail(
                            context.contentResolver,
                            id,
                            MediaStore.Images.Thumbnails.MINI_KIND,
                            null
                        ).asImageBitmap()
                    }

                    imageDataItemMutableList += ImageDataItem(
                        id, contentUri, name, size, thumbnail
                    )
                    setLoadPercent(processNum * 100 / picNum)
                    processNum++
                }
            }
            setImageList(imageDataItemMutableList.toList())
            setLoading(false)
        }
    }

    fun isPermissionGranted(): Boolean {
        var granted = true
        storagePermissionsState.permissions.forEach {
            granted = granted && it.status.isGranted
        }
        return granted
    }

    LaunchedEffect(permissionGranted) {
        if (permissionGranted) {
            readPictures()
        } else {
            if (!loading) {
                setImageList(listOf())
                setPictureNum(0)
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (isRequestPermission) {
                    isRequestPermission = false
                } else {
                    shouldCheckPermission()
                }
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(checkPermission) {
        if (isPermissionGranted()) {
            setPermissionTip("权限正常")
            setPermissionGranted(true)
        } else {
            setPermissionGranted(false)
            isRequestPermission = true
            storagePermissionsState.launchMultiplePermissionRequest()
        }
    }

    LaunchedEffect(isPermissionReqEnd) {
        if (isPermissionReqEnd) {
            if (isPermissionGranted()) {
                setPermissionTip("权限正常")
                setPermissionGranted(true)
            } else {
                setPermissionGranted(false)
                if (storagePermissionsState.shouldShowRationale) {
                    setPermissionTip("你拒绝了本次的权限请求")
                } else {
                    setPermissionTip("你已永久拒绝存储权限\n可以到手机权限设置中授予本应用权限")
                }
            }
            isPermissionReqEnd = false
        }
    }

    Column {
        Text(
            "可以读取到的图片共有${pictureNum}张",
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(0.dp, 6.dp)
        )
        if (permissionGranted) {
            if (loading) {
                Row(
                    Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterVertically
                ) {
                    Text(text = "读取照片中... $loadPercent%", Modifier.padding(end = 8.dp))
                    CircularProgressIndicator()
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(items = imageList) {
                        Image(
                            bitmap = it.thumbnail,
                            contentDescription = it.name,
                            alignment = Alignment.Center,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(CurveCornerShape(3.dp))
                                .weight(1f)
                                .aspectRatio(1f)
                        )
                    }
                }
            }
        } else {
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(50.dp, 0.dp),
                Arrangement.Center,
                Alignment.CenterVertically
            ) {
                Text(text = permissionTip, textAlign = TextAlign.Center)
            }
        }
    }
}