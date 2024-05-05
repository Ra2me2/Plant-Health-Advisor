package com.example.planter_app.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.planter_app.ConnectivityCheck
import com.example.planter_app.MyApplication
import com.example.planter_app.firebase_login.sign_in.GoogleAuthUiClient
import com.example.planter_app.firebase_login.sign_in.SignInScreen
import com.example.planter_app.screens.home.HomeScreen
import com.google.android.gms.auth.api.identity.Identity
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

    fun isUserLoggedIn(): Boolean{
        val googleAuthUiClient by lazy {
            GoogleAuthUiClient(
                context = MyApplication.instance!!.applicationContext,
                oneTapClient = Identity.getSignInClient(MyApplication.instance!!.applicationContext)
            )
        }
        return googleAuthUiClient.getSignedInUser() != null
    }

}