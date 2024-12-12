package com.example.parkease.composables

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import com.example.parkease.ui.theme.AppTheme
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import java.util.EnumMap

private const val qrCodeWidthPixels = 500

fun generateQRCode(
    data: String,
    qrCodeColor: Int,
    backgroundColor: Int
): Bitmap? {
    val bitMatrix: BitMatrix = try {
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
        MultiFormatWriter().encode(
            data,
            BarcodeFormat.QR_CODE,
            qrCodeWidthPixels,
            qrCodeWidthPixels,
            hints
        )
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }

    val qrCodeWidth = bitMatrix.width
    val qrCodeHeight = bitMatrix.height
    val pixels = IntArray(qrCodeWidth * qrCodeHeight)

    for (y in 0 until qrCodeHeight) {
        val offset = y * qrCodeWidth
        for (x in 0 until qrCodeWidth) {
            pixels[offset + x] = if (bitMatrix[x, y]) {
                qrCodeColor
            } else {
                backgroundColor
            }
        }
    }

    val bitmap = Bitmap.createBitmap(qrCodeWidth, qrCodeHeight, Bitmap.Config.RGB_565)
    bitmap.setPixels(pixels, 0, qrCodeWidth, 0, 0, qrCodeWidth, qrCodeHeight)

    return bitmap
}

@Composable
fun QRCodeComposable(
    data: String,
    modifier: Modifier = Modifier
) {
    val qrCodeColor = AppTheme.colorScheme.primary.toArgb()
    val backgroundColor = AppTheme.colorScheme.secondary.toArgb()

    val qrCodeBitmap = generateQRCode(
        data = data,
        qrCodeColor = qrCodeColor,
        backgroundColor = backgroundColor
    )

    if (qrCodeBitmap != null) {
        androidx.compose.foundation.Image(
            bitmap = qrCodeBitmap.asImageBitmap(),
            contentDescription = "QR Code",
            modifier = modifier
        )
    }
}
