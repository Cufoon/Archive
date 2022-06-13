package cufoon.ddns.util

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class CurveCornerShape(private val rate: Float = 0.02f) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val w2 = size.width / 2f
        val h2 = size.height / 2f
        val wl = size.width * rate
        val hl = size.height / size.width * wl

        val path = Path().apply {
            moveTo(0f, h2)
            cubicTo(0f, hl, wl, 0f, w2, 0f)
            cubicTo(size.width - wl, 0f, size.width, hl, size.width, h2)
            cubicTo(size.width, size.height - hl, size.width - wl, size.height, w2, size.height)
            cubicTo(wl, size.height, 0f, size.height - hl, 0f, h2)
            close()
        }
        return Outline.Generic(path)
    }
}