package com.example.ocrzebra.ui

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var imageUri = mutableStateOf<Uri?>(null)
}