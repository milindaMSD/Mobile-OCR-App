// NavGraph.kt
package com.example.ocrzebra.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ocrzebra.ui.SharedViewModel
import com.example.ocrzebra.ui.screens.OcrScreen
import com.example.ocrzebra.ui.screens.UploadScreen

@Composable
fun NavGraph(sharedViewModel: SharedViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "upload") {
        composable("upload") { UploadScreen(navController, sharedViewModel) }
        composable("ocr") { OcrScreen(sharedViewModel) }
    }
}
