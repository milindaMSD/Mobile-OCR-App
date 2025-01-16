// OcrScreen.kt
package com.example.ocrzebra.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.example.ocrzebra.ui.SharedViewModel
import com.example.ocrzebra.utils.convertToText
import java.io.InputStream

@Composable
fun OcrScreen(sharedViewModel: SharedViewModel) {

    val context = LocalContext.current
    var text by remember { mutableStateOf("") }


    val imageUri = sharedViewModel.imageUri.value
    val bitmap: Bitmap? = imageUri?.let {
        val inputStream: InputStream? = context.contentResolver.openInputStream(it)
        BitmapFactory.decodeStream(inputStream)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Selected Image" ,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray)
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                bitmap?.let {
                    text = convertToText(context, it)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Convert to Text")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Extracted Text",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = text,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        )
    }
}
