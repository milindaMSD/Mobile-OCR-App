package com.example.ocrzebra.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ocrzebra.ui.SharedViewModel
import com.example.ocrzebra.utils.convertToText
import java.io.InputStream

@Composable
fun OcrScreen(sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var text by remember { mutableStateOf("") }
    var showSnackbar by remember { mutableStateOf(false) }

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
                contentDescription = "Selected Image",
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
            Icon(
                Icons.Rounded.Build,
                contentDescription = "Proceed to OCR"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Convert to Text")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (text.isNotEmpty()) {
            Text(
                text = "Extracted Text",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            SelectionContainer {
                BasicText(
                    text = text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .border(2.dp, Color.LightGray, RoundedCornerShape(8.dp))
                        .padding(16.dp)
                        .background(Color.Unspecified),

//                    borderColor = Color.Black,
                    style = MaterialTheme.typography.labelLarge,

                )

            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    clipboardManager.setText(AnnotatedString(text))
                    showSnackbar = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black // Set the button background color
                ),
                shape = RoundedCornerShape(10.dp),
//                modifier = Modifier.background(Color.LightGray)

            ) {
                Icon(
                    Icons.Rounded.Done,
                    contentDescription = "Proceed to OCR"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Copy to Clipboard")
            }
        }

        if (showSnackbar) {
            Snackbar(
                action = {
                    TextButton(onClick = { showSnackbar = false }) {
                        Text("Dismiss")
                    }
                }
            ) {
                Text("Text copied to clipboard")
            }
        }
    }
}
