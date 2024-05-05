package com.example.planter_app.navigation_drawer


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.planter_app.R
import com.example.planter_app.screens.home.HomeScreenContent
import com.example.planter_app.screens.settings.SettingsViewModel
import com.example.planter_app.ui.theme.Planter_appTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    onNavigationIconClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    titleComingFromPreviews: String?= null
) {

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = titleComingFromPreviews?: SettingsViewModel.appBarTitle.value,
                color = MaterialTheme.colorScheme.primary,
            )
        },
        navigationIcon = {
            if (SettingsViewModel.appBarTitle.value != stringResource(id = R.string.SIGN_IN_SCREEN_TITLE)) {
                IconButton(onClick = { onNavigationIconClick() }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Toggle drawer"
                    )
                }
            }

        },
        scrollBehavior = scrollBehavior,

        actions = {
            // for any action bar item... maybe notifications in future
            if (SettingsViewModel.appBarTitle.value != stringResource(id = R.string.SIGN_IN_SCREEN_TITLE)) {
                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = "NotificationsNone"
                    )
                }
            }

        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun AppBarPreview() {
    Planter_appTheme {
        AppBar(
            onNavigationIconClick = {},
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            titleComingFromPreviews = "App Bar"
        )
    }
}
