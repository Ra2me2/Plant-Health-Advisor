package com.example.planter_app.screens.my_plants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import com.example.planter_app.R

import com.example.planter_app.screens.settings.SettingsViewModel
import java.util.Locale

object MyPlantsScreen:Screen {

    @Composable
    override fun Content() {
        SettingsViewModel.appBarTitle.value = stringResource(id = R.string.MY_PLANTS_SCREEN_TITLE)

        val viewmodel = viewModel<SettingsViewModel>()

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "My Plants")

        }
    }
}