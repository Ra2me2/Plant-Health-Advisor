package com.example.planter_app.screens.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.TagFaces
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.example.planter_app.R
import com.example.planter_app.navigation_drawer.AppBar
import com.example.planter_app.screens.home.HomeScreenContent
import com.example.planter_app.screens.settings.SettingsViewModel
import com.example.planter_app.ui.theme.Planter_appTheme

object AboutScreen : Screen {

    @Composable
    override fun Content() {
        SettingsViewModel.appBarTitle.value = stringResource(id = R.string.ABOUT_SCREEN_TITLE)

        AboutScreenContent()
    }
}

@Composable
fun AboutScreenContent(

) {
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

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun AboutScreenPreview() {
    Planter_appTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    AppBar(
                        onNavigationIconClick = {},
                        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                        titleComingFromPreviews = stringResource(id = R.string.ABOUT_SCREEN_TITLE)
                    )
                }
            ) { paddingVales ->
                Spacer(modifier = Modifier.padding(top = paddingVales.calculateTopPadding()))
                AboutScreenContent()
            }
        }
    }
}