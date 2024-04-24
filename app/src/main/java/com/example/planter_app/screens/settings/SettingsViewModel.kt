package com.example.planter_app.screens.settings

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.example.planter_app.R
import java.util.Locale

class SettingsViewModel:ViewModel(){

    companion object{
        var appBarTitle = mutableStateOf("")

        val darkMode = mutableStateOf(false)
        val dynamicTheme = mutableStateOf(false)

    }


}