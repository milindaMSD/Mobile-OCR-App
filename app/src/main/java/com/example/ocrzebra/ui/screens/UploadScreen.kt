// UploadScreen.kt
package com.example.ocrzebra.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ocrzebra.ui.SharedViewModel
import java.io.InputStream

@Composable
fun UploadScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        sharedViewModel.imageUri.value = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (sharedViewModel.imageUri.value == null) {
            Button(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pick Image")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        sharedViewModel.imageUri.value?.let {
            Column {
                Text(text = "Image Selected :" + it.toString())
//                Text(text = )
                Spacer(modifier = Modifier.height(8.dp))
                val context = LocalContext.current
                val imageUri = sharedViewModel.imageUri.value
                val bitmap: Bitmap? = imageUri?.let {
                    val inputStream: InputStream? = context.contentResolver.openInputStream(it)
                    BitmapFactory.decodeStream(inputStream)
                }
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray)
                        .fillMaxWidth()

//                    backgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
                )
                Spacer(modifier = Modifier.height(25.dp))
            }
            Button(
                onClick = {
                    navController.navigate("ocr")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Proceed to OCR")
            }
        }
    }
}
