package com.example.parvaznama.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap

fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(context, drawableId)!!
    val bitmap = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}