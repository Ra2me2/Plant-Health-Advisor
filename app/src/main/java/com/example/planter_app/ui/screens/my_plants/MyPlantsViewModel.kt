package com.example.planter_app.ui.screens.my_plants

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyPlantsViewModel:ViewModel() {

    companion object{
        var plantDeleteIcon = mutableStateOf(false)
    }

    private val _imageClicked = MutableStateFlow(false)
    val imageClicked: StateFlow<Boolean> = _imageClicked.asStateFlow()

    fun setImageClicked(boolean: Boolean){
        _imageClicked.value = boolean
    }


    private val _plantNetApiResponse = MutableStateFlow<String?>(null)
    val plantNetApiResponse: StateFlow<String?> = _plantNetApiResponse.asStateFlow()

    fun setPlantNetApiResponse(string: String){
        _plantNetApiResponse.value = string
    }


}