// UploadScreen.kt
package com.example.ocrzebra.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ocrzebra.ui.SharedViewModel

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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { imagePickerLauncher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pick Image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        sharedViewModel.imageUri.value?.let {
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
