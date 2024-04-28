package com.example.planter_app.screens.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planter_app.ConnectivityCheck
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel:ViewModel(){

    companion object{
        var appBarTitle = mutableStateOf("")

        val darkMode = mutableStateOf(false)
        val dynamicTheme = mutableStateOf(false)
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _isNetworkAvailable = MutableStateFlow(ConnectivityCheck.checkNetworkAvailability())
    val isNetworkAvailable: StateFlow<Boolean> = _isNetworkAvailable.asStateFlow()
    fun updateConnectionStatus(){
        _isNetworkAvailable.value = ConnectivityCheck.checkNetworkAvailability()
    }

    fun updateRefresh() {
        _isRefreshing.value = true
        updateConnectionStatus()

        viewModelScope.launch {
            delay(1000L)
            _isRefreshing.value = false
        }
    }

}