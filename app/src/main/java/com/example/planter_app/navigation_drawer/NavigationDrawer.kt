package com.example.planter_app.navigation_drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.planter_app.R
import com.example.planter_app.screens.about.AboutScreen
import com.example.planter_app.screens.home.HomeScreen
import com.example.planter_app.screens.my_plants.MyPlantsScreen
import com.example.planter_app.screens.settings.SettingsScreen
import com.example.planter_app.screens.settings.SettingsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    scope: CoroutineScope,
    paddingValues: PaddingValues,
    navigator: Navigator,
) {
    val selectedNavItem = remember { mutableStateOf(Icons.Outlined.Home) }
    val localNavigator = LocalNavigator.currentOrThrow

    ModalNavigationDrawer(
        modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
        drawerState = drawerState,
        gesturesEnabled = SettingsViewModel.appBarTitle.value != stringResource(id = R.string.SIGN_IN_SCREEN_TITLE),
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(20.dp))

                // header image
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painterResource(R.drawable.logo),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape),
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 20.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.secondary.copy(0.2f)
                )

                // nav items
                LazyColumn {
                    item {
                        NavigationDrawerItem(
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                            icon = {
                                Icon(
                                    imageVector = if (Icons.Outlined.Home == selectedNavItem.value) Icons.Filled.Home else Icons.Outlined.Home,
                                    contentDescription = "Home"
                                )
                            },
                            label = { Text(text = stringResource(id = R.string.home)) },
                            selected = Icons.Outlined.Home == selectedNavItem.value,
                            onClick = {
                                selectedNavItem.value = Icons.Outlined.Home
                                localNavigator.push(HomeScreen)
                                scope.launch {drawerState.close()}

                            }
                        )

                        NavigationDrawerItem(
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                            icon = {
                                Icon(
                                    imageVector = if (Icons.Outlined.Eco == selectedNavItem.value) Icons.Filled.Eco else Icons.Outlined.Eco,
                                    contentDescription = "My Plants"
                                )
                            },
                            label = { Text(text = stringResource(id = R.string.my_plants)) },
                            selected = Icons.Outlined.Eco == selectedNavItem.value,
                            onClick = {
                                selectedNavItem.value = Icons.Outlined.Eco
                                localNavigator.push(MyPlantsScreen)
                                scope.launch {drawerState.close()}

                            }
                        )

                        NavigationDrawerItem(
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                            icon = {
                                Icon(
                                    imageVector = if (Icons.Outlined.Settings == selectedNavItem.value) Icons.Filled.Settings else Icons.Outlined.Settings,
                                    contentDescription = "Settings"
                                )
                            },
                            label = { Text(text = stringResource(id = R.string.settings)) },
                            selected = Icons.Outlined.Settings == selectedNavItem.value,
                            onClick = {
                                selectedNavItem.value = Icons.Outlined.Settings
                                localNavigator.push(SettingsScreen)
                                scope.launch {drawerState.close()}
                            }
                        )

                        NavigationDrawerItem(
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                            icon = {
                                Icon(
                                    imageVector = if (Icons.Outlined.Info == selectedNavItem.value) Icons.Filled.Info else Icons.Outlined.Info,
                                    contentDescription = "About"
                                )
                            },
                            label = { Text(text = stringResource(id = R.string.about)) },
                            selected = Icons.Outlined.Info == selectedNavItem.value,
                            onClick = {
                                selectedNavItem.value = Icons.Outlined.Info
                                localNavigator.push(AboutScreen)
                                scope.launch {drawerState.close()}

                            }
                        )
                    }
                }

            }
        },
        content = {
            CurrentScreen()
        })

}
