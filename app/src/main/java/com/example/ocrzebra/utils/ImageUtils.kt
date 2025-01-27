package com.example.ocrzebra.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap {
    val inputStream = context.contentResolver.openInputStream(uri)
    return BitmapFactory.decodeStream(inputStream).also {
        inputStream?.close()
    }
}

fun convertToText(context: Context, bitmap: Bitmap, ): String {
    val tessBaseAPI = TessBaseAPI()
    val dataPath = context.filesDir.toString() + "/"
    val lang = "can"
    val tessDataDir = File(dataPath + "tessdata/")
    val tessDataFile = File(tessDataDir, "$lang.traineddata")
    println("[debug - milinda] tessDataFile: $tessDataFile")
    if (!tessDataFile.exists()) {
        println("[debug - milinda] tessDataFile does not exist")
        tessDataDir.mkdirs()
        copyTessDataFile(context, lang)
    }

    tessBaseAPI.init(dataPath, lang)
    tessBaseAPI.setImage(bitmap)
    val extractedText = tessBaseAPI.utF8Text
    tessBaseAPI.end()

    println("[debug - milinda] extractedText: $extractedText")

    return extractedText

}

private fun copyTessDataFile(context: Context, lang: String) {
    try {
        val assetManager = context.assets
        val inputStream: InputStream = assetManager.open("tessdata/$lang.traineddata")
        val outFile = File(context.filesDir, "tessdata/$lang.traineddata")
        val outStream = FileOutputStream(outFile)

//        debug
        println("[debug - milinda] outFile: $outFile")

        val buffer = ByteArray(1024)
        var read: Int
        while (inputStream.read(buffer).also { read = it } != -1) {
            outStream.write(buffer, 0, read)
        }

        inputStream.close()
        outStream.flush()
        outStream.close()
        println("[debug - milinda] Copied $lang.traineddata")
    } catch (e: IOException) {
        e.printStackTrace()
    }
}