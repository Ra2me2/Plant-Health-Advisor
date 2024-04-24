package com.example.planter_app.screens.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import com.example.planter_app.MyApplication
import com.example.planter_app.R
import com.example.planter_app.firebase_login.sign_in.GoogleAuthUiClient
import com.example.planter_app.firebase_login.sign_in.SignInScreen
import com.example.planter_app.firebase_login.sign_in.SignInViewModel
import com.example.planter_app.ui.theme.Planter_appTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch


object SettingsScreen : Screen {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = MyApplication.instance!!.applicationContext,
            oneTapClient = Identity.getSignInClient(MyApplication.instance!!.applicationContext)
        )
    }

    private const val normalTheme = "NormalTheme"
    private const val dynamicTheme = "DynamicTheme"

    @Composable
    override fun Content() {
        SettingsViewModel.appBarTitle.value = stringResource(id = R.string.SETTINGS_SCREEN_TITLE)


        val navigator = LocalNavigator.currentOrThrow

        val signInScreenViewModel = viewModel<SignInViewModel>()
        val viewModel = viewModel<SettingsViewModel>()

        val profilePicture = if (googleAuthUiClient.getSignedInUser()?.profilePictureURL != null) {
            googleAuthUiClient.getSignedInUser()?.profilePictureURL
        } else {
            R.drawable.guest_user_profile_dp
        }

        val userName = if (googleAuthUiClient.getSignedInUser()?.username != null) {
            googleAuthUiClient.getSignedInUser()?.username
        } else {
            "Guest User"
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            AsyncImage(
                model = profilePicture,
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = userName.toString(),
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier
                    .padding(vertical = 20.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 35.dp, end = 35.dp, bottom = 10.dp)
            ) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SwitchSettings(
                            text = stringResource(id = R.string.dark_mode),
                            setting = normalTheme
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 10.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.secondary.copy(0.2f)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SwitchSettings(
                            text = stringResource(id = R.string.dynamic_theme),
                            setting = dynamicTheme
                        )
                    }

//                    Text(text = "${googleAuthUiClient.getSignedInUser()?.userId}")

                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // sign in option only for anonymous users
                if (googleAuthUiClient.getSignedInUser()?.username == null) {
                    SignInOutButtons(
                        buttonText = stringResource(id = R.string.sign_in),
                        onClick = { /*TODO*/ })
                }

                SignInOutButtons(
                    buttonText = stringResource(id = R.string.sign_out),
                    onClick = {
                        viewModel.viewModelScope.launch {
                            googleAuthUiClient.signOut()
                            Toast.makeText(
                                MyApplication.instance!!.applicationContext,
                                "Signed out",
                                Toast.LENGTH_SHORT
                            ).show()
                            navigator.replaceAll(SignInScreen)
                        }
                    })
            }
        }
    }

    @Composable
    private fun SignInOutButtons(
        buttonText: String,
        onClick: () -> Unit
    ) {
        Button(
            modifier = Modifier
                .padding(bottom = 20.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp,
                pressedElevation = 5.dp
            ),
            onClick = {
                onClick()
            }) {
            Text(text = buttonText)
        }
    }

    @Composable
    private fun RowScope.SwitchSettings(text: String, setting: String) {

        Text(
            text = text,
            style = TextStyle(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.primary,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize
        )
        Spacer(modifier = Modifier.weight(1f)) // Pushes the Switch to the right end

        Switch(
            checked = if (setting == normalTheme) SettingsViewModel.darkMode.value else SettingsViewModel.dynamicTheme.value,
            onCheckedChange = {
                if (setting == normalTheme) {
                    SettingsViewModel.darkMode.value = !SettingsViewModel.darkMode.value
                } else {
                    SettingsViewModel.dynamicTheme.value = !SettingsViewModel.dynamicTheme.value
                }
            },
            modifier = Modifier
                .size(50.dp)
                .padding(horizontal = 16.dp),
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    Planter_appTheme {

    }
}