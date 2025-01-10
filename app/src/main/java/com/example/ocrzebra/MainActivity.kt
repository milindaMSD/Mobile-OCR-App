package com.example.ocrzebra

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ocrzebra.ui.theme.OCRzebraTheme
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        copyTessDataFiles()
        setContent {
            OCRzebraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        activity = this,
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun copyTessDataFiles() {
        val assetManager = assets
        val tessDataPath = filesDir.toString() + "/tessdata/"
        val tessDataFile = File(tessDataPath)

        if (!tessDataFile.exists()) {
            tessDataFile.mkdirs()
        }

        try {
            for (fileName in assetManager.list("tessdata")!!) {
                val file = File(tessDataPath + fileName)
                if (!file.exists()) {
                    assetManager.open("tessdata/$fileName").use { `in` ->
                        FileOutputStream(file).use { out ->
                            `in`.copyTo(out)
                        }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(activity: ComponentActivity, name: String, modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("Zebra OCR Engine ...") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "OCR Engine") },
                navigationIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.zebra_logo),
                        contentDescription = "Android Logo",
                        modifier = Modifier.size(128.dp)
                    )
                }
            )
        },
        content = { innerPadding ->
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.eurotext),
                        contentDescription = "Text image",
                        modifier = Modifier.size(500.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            val bitmap = BitmapFactory.decodeResource(activity.resources, R.drawable.eurotext)
                            text = convertToText(activity, bitmap)
                        },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Convert to Text")
                    }
                }
                Box(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text)
                }
            }
        }
    )
}

fun convertToText(context: Context, bitmap: Bitmap): String {
    val tessBaseAPI = TessBaseAPI()
    val dataPath = context.filesDir.toString() + "/"
    val lang = "eng"

    // Check if the tessdata directory exists
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OCRzebraTheme{
        Greeting(activity = MainActivity(), name = "Android")
    }
}