package com.example.ocrzebra

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ocrzebra.ui.theme.OCRzebraTheme
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


var imageUrl = "image.jpg"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        copyTessDataFiles()
        setContent {
            OCRzebraTheme {
                MyApp()
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
fun MyApp() {
    Scaffold(
        topBar = { AppBar() },
        content = { innerPadding ->
            MainContent(Modifier.padding(innerPadding))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar() {
    TopAppBar(
        title = {
            Text(
                text = "OCR Engine",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },

        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.zebra_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    )
}

@Composable
fun MainContent(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf(" ") }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val bitmap = loadBitmapFromAssets(context, imageUrl)
        Image(

            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Text Image",
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
                .fillMaxWidth()
        )



        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val bitmap = loadBitmapFromAssets(context, imageUrl)
                text = convertToText(context, bitmap)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)


        ) {
            Text("Convert to Text")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Box {
            Text(
                text = "Extracted Text",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                color = Color.DarkGray
            )
        }


    }
}

fun loadBitmapFromAssets(context: Context, fileName: String): Bitmap {
    val assetManager = context.assets
    val inputStream = assetManager.open(fileName)
    return BitmapFactory.decodeStream(inputStream).also {
        inputStream.close()
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

@Preview(showBackground = true)
@Composable
fun MyAppPreview() {
    OCRzebraTheme {
        MyApp()
    }
}
