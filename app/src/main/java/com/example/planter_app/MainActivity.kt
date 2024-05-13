package com.example.planter_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.Navigator
import com.example.planter_app.ui.theme.Planter_appTheme
import com.example.planter_app.appbar_and_navigation_drawer.AppBar
import com.example.planter_app.appbar_and_navigation_drawer.NavigationDrawer
import com.example.planter_app.screens.home.HomeScreen
import com.example.planter_app.screens.settings.SettingsViewModel
import com.example.planter_app.screens.sign_in.SignInScreen
import com.example.planter_app.shared_preferences.ThemePreferences
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scope = rememberCoroutineScope()

            val settingsViewModel = viewModel<SettingsViewModel>()

            // if user is logged in, then go to home screen. else sign in screen
            val userLoggedInStatus = settingsViewModel.isUserLoggedIn()

            val themePreferences = ThemePreferences(this)
            SettingsViewModel.darkMode.value = themePreferences.loadDarkTheme()
            SettingsViewModel.dynamicTheme.value = themePreferences.loadDynamicTheme()

            Planter_appTheme(
                darkTheme = SettingsViewModel.darkMode.value,
                dynamicColor = SettingsViewModel.dynamicTheme.value
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

                    //scrollBehaviour is to implement app bar color change once user starts scrolling the screen items
                    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

                    Scaffold(
                        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                        topBar = {
                            AppBar(
                                onNavigationIconClick = {
                                    scope.launch {
                                        if (drawerState.isClosed) {
                                            drawerState.open()
                                        } else drawerState.close()
                                    }
                                },
                                scrollBehavior
                            )
                        }
                    ) { paddingVales ->
                        //Initial screen -> SignIn screen
                        Navigator(
                            screen = if (userLoggedInStatus) HomeScreen else SignInScreen
                        )
                        { navigator ->
                            NavigationDrawer(
                                drawerState = drawerState,
                                scope = scope,
                                paddingValues = paddingVales,
                                navigator = navigator
                            )
                        }
                    }
                }
            }
        }
    }
}


