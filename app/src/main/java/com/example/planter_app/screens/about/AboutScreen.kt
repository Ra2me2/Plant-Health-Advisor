package com.example.planter_app.screens.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.TagFaces
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.example.planter_app.R
import com.example.planter_app.screens.settings.SettingsViewModel

object AboutScreen : Screen {

    @Composable
    override fun Content() {
        SettingsViewModel.appBarTitle.value = stringResource(id = R.string.ABOUT_SCREEN_TITLE)


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "We write some fancy things about the app and us here",
                textAlign = TextAlign.Center
            )
            Image(imageVector = Icons.Rounded.TagFaces, contentDescription = "")

        }
    }

}