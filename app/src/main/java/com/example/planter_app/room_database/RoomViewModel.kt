package com.example.planter_app.room_database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RoomViewModel(application: Application): AndroidViewModel(application) {
    private val repository: RoomRepository
    val readAllData: Flow<List<PlantTable>>

    init {
        val plantDao = AppDatabase.getDatabase(application).plantDao()
        repository = RoomRepository(plantDao) // Initialize the repository here
        readAllData = repository.readAllData
    }

    fun addData(data: PlantTable) {
        viewModelScope.launch {
            repository.addData(data)
        }
    }

    fun updateData(data: PlantTable) {
        viewModelScope.launch {
            repository.updateData(data)
        }
    }

    fun deleteData(data: PlantTable) {
        viewModelScope.launch {
            repository.deleteData(data)
        }
    }

    fun deleteAllData() {
        viewModelScope.launch {
            repository.deleteAllData()
        }
    }
}
