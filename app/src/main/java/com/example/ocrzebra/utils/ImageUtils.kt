package com.example.ocrzebra.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap {
    val inputStream = context.contentResolver.openInputStream(uri)
    return BitmapFactory.decodeStream(inputStream).also {
        inputStream?.close()
    }
}

fun convertToText(context: Context, bitmap: Bitmap): String {
    val tessBaseAPI = TessBaseAPI()
    val dataPath = context.filesDir.toString() + "/"
    val lang = "eng"

    val tessDataDir = File(dataPath + "tessdata/")
    if (!tessDataDir.exists() || !tessDataDir.isDirectory) {
        throw IllegalArgumentException("Data path must contain subfolder tessdata!")
    }

    tessBaseAPI.init(dataPath, lang)
    tessBaseAPI.setImage(bitmap)
    val extractedText = tessBaseAPI.utF8Text
    tessBaseAPI.end()

    return extractedText
}
