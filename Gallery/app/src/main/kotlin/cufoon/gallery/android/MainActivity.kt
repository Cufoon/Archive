package cufoon.gallery.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cufoon.gallery.android.page.MainPage
import cufoon.gallery.android.ui.theme.CufoonGalleryTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CufoonGalleryTheme {
                Surface(Modifier.fillMaxSize()) {
                    MainPage()
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:id=reference_phone,shape=Normal,width=1080,height=2400,unit=dp,dpi=440"
)
@Composable
fun DefaultPreview() {
    CufoonGalleryTheme {
        Surface(Modifier.fillMaxSize()) {
            MainPage()
        }
    }
}
