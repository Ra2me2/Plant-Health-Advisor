package com.example.planter_app.navigation_drawer


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.planter_app.R
import com.example.planter_app.screens.settings.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    onNavigationIconClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = SettingsViewModel.appBarTitle.value,
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
