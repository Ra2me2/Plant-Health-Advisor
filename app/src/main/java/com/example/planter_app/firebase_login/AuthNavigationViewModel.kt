package com.example.planter_app.firebase_login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.planter_app.firebase_login.sign_in.GoogleAuthUiClient
import com.google.android.gms.auth.api.identity.Identity

class AuthNavigationViewModel: ViewModel() {

    companion object{
        val signedIn = mutableStateOf(false)

    }


}