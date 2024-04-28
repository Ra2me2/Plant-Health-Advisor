package com.example.planter_app.screens.home

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.planter_app.ConnectivityCheck
import com.example.planter_app.screens.settings.SettingsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay


class HomeViewModel:ViewModel() {
    private var _imageUri = MutableStateFlow<List<Uri?>>(emptyList())
    val imageUri: StateFlow<List<Uri?>> = _imageUri.asStateFlow()

    fun setImageUri(uri: List<Uri?>) {
        _imageUri.value = uri
    }

}