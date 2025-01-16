package com.example.ocrzebra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ocrzebra.ui.navigation.NavGraph
import com.example.ocrzebra.ui.theme.OCRzebraTheme
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import com.example.ocrzebra.ui.SharedViewModel
import com.example.ocrzebra.ui.components.AppBar

class MainActivity : ComponentActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OCRzebraTheme {
                Column {
                    AppBar()
                    NavGraph(sharedViewModel)
                }
            }
        }
    }
}


