package cufoon.gallery.android.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)

class CurveCornerShape(private val radius: Dp = 0.dp) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        var r = with(density) {
            this@CurveCornerShape.radius.roundToPx().toFloat()
        }
        val w = size.width
        val w2 = w / 2
        val h = size.height
        val h2 = h / 2
        if (r > w2) {
            r = w2
        }
        if (r > h2) {
            r = h2
        }
        val rMagic = r * 2 / 5
        val path = Path().apply {
            moveTo(0f, r)
            cubicTo(0f, rMagic, rMagic, 0f, r, 0f)
            lineTo(w - r, 0f)
            cubicTo(w - rMagic, 0f, w, rMagic, w, r)
            lineTo(w, h - r)
            cubicTo(w, h - rMagic, w - rMagic, h, w - r, h)
            lineTo(r, h)
            cubicTo(rMagic, h, 0f, h - rMagic, 0f, h - r)
            close()
        }
        return Outline.Generic(path)
    }
}
