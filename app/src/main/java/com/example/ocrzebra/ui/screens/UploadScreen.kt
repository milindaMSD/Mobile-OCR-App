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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ocrzebra.R
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

            Image(
                painter = painterResource(id = R.drawable.tesseractocr),
                contentDescription = "App Logo",
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .size(200.dp)
            )
            Spacer(modifier =Modifier.height(20.dp))
            Text(text = "Tesseract OCR Engine",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold



//                bold

            )
            Spacer(modifier =Modifier.height(20.dp))

            Button(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f),
                shape = RoundedCornerShape(10.dp),



            ) {
                Icon(
                    Icons.Rounded.AddCircle,
                    contentDescription ="Proceed to OCR")
                Spacer(modifier =Modifier.width(8.dp))
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
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier
                    .fillMaxWidth(),
//                    .fillMaxHeight(0.2f),
//                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black // Set the button background color
                ),



                ) {
                Icon(
                    Icons.Rounded.AddCircle,
                    contentDescription ="Proceed to OCR")
                Spacer(modifier =Modifier.width(8.dp))
                Text("Pick Another Image")
            }
            Spacer(modifier =Modifier.height(8.dp))
            Button(
                onClick = {
                    navController.navigate("ocr")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Rounded.PlayArrow,
                    contentDescription ="Proceed to OCR")
                Spacer(modifier =Modifier.width(8.dp))
                Text("Proceed to OCR")
            }

        }
    }
}
