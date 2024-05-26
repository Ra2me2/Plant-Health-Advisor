package com.example.planter_app.ui.screens.my_plants

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.planter_app.firebase_realtime_database.Plant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyPlantsViewModel:ViewModel() {

    companion object{
        var triggerPlantDeleteBottomSheet = mutableStateOf(false)
        var displayPlantDeleteBottomSheet = mutableStateOf(false)
    }

    // Plant Details Screen
    private val _isImageExpanded = MutableStateFlow(false)
    val isImageExpanded : StateFlow<Boolean> = _isImageExpanded.asStateFlow()

    fun setIsImageExpanded(boolean: Boolean){
        _isImageExpanded.value = boolean
    }



    // My Plants Screen
    private val _plantsList = MutableStateFlow<List<Plant>?>(null)
    val plantsList: StateFlow<List<Plant>?> = _plantsList.asStateFlow()

    fun setPlantsList(list:List<Plant>){
        _plantsList.value = list
    }

}