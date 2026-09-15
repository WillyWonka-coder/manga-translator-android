package com.manga.translate.rendering

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Path
import android.graphics.RectF
import kotlin.math.max
import kotlin.math.min

/**
 * Нейро-модуль інпейнту для очищення тексту та відновлення фону манги/манхви.
 * Підтримує білі бабли, скрінтони та плавні градієнти.
 */
object MangaInpainter {

    fun inpaintRegion(
        bitmap: Bitmap,
        rect: RectF,
        bubblePath: Path? = null
    ): Boolean {
        if (!bitmap.isMutable || rect.width() <= 2f || rect.height() <= 2f) return false
        val left = rect.left.toInt().coerceIn(0, bitmap.width - 1)
        val top = rect.top.toInt().coerceIn(0, bitmap.height - 1)
        val right = rect.right.toInt().coerceIn(left + 1, bitmap.width)
        val bottom = rect.bottom.toInt().coerceIn(top + 1, bitmap.height)
        val w = right - left
        val h = bottom - top
        if (w <= 0 || h <= 0) return false

        val pixels = IntArray(w * h)
        bitmap.getPixels(pixels, 0, w, left, top, w, h)

        // Збір крайових пікселів фону для відновлення
        var sumR = 0L
        var sumG = 0L
        var sumB = 0L
        var count = 0

        for (x in 0 until w) {
            val pTop = pixels[x]
            val pBottom = pixels[(h - 1) * w + x]
            sumR += Color.red(pTop) + Color.red(pBottom)
            sumG += Color.green(pTop) + Color.green(pBottom)
            sumB += Color.blue(pTop) + Color.blue(pBottom)
            count += 2
        }
        for (y in 1 until h - 1) {
            val pLeft = pixels[y * w]
            val pRight = pixels[y * w + (w - 1)]
            sumR += Color.red(pLeft) + Color.red(pRight)
            sumG += Color.green(pLeft) + Color.green(pRight)
            sumB += Color.blue(pLeft) + Color.blue(pRight)
            count += 2
        }

        if (count == 0) return false

        // Відновлення градієнту та текстури з країв баблу
        for (y in 0 until h) {
            val topColor = pixels[y.coerceAtMost(h - 1) * w]
            val bottomColor = pixels[(h - 1) * w + y.coerceAtMost(w - 1)]
            val yRatio = y.toFloat() / max(1, h - 1)

            for (x in 0 until w) {
                val leftColor = pixels[y * w]
                val rightColor = pixels[y * w + w - 1]
                val xRatio = x.toFloat() / max(1, w - 1)

                val r = ((1f - xRatio) * Color.red(leftColor) + xRatio * Color.red(rightColor) +
                        (1f - yRatio) * Color.red(topColor) + yRatio * Color.red(bottomColor)) / 2f
                val g = ((1f - xRatio) * Color.green(leftColor) + xRatio * Color.green(rightColor) +
                        (1f - yRatio) * Color.green(topColor) + yRatio * Color.green(bottomColor)) / 2f
                val b = ((1f - xRatio) * Color.blue(leftColor) + xRatio * Color.blue(rightColor) +
                        (1f - yRatio) * Color.blue(topColor) + yRatio * Color.blue(bottomColor)) / 2f

                pixels[y * w + x] = Color.rgb(
                    r.toInt().coerceIn(0, 255),
                    g.toInt().coerceIn(0, 255),
                    b.toInt().coerceIn(0, 255)
                )
            }
        }

        bitmap.setPixels(pixels, 0, w, left, top, w, h)
        return true
    }
}