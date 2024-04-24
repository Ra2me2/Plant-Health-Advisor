package com.example.planter_app.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.planter_app.R
import com.example.planter_app.screens.settings.SettingsViewModel

object HomeScreen : Screen {

    @Composable
    override fun Content() {
        SettingsViewModel.appBarTitle.value = stringResource(id = R.string.HOME_SCREEN_TITLE)

        val navigator = LocalNavigator.currentOrThrow
        val viewModel = viewModel<HomeViewModel>()

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            val painter =
                rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = R.drawable.tree_robots_12)
                        .apply(block = fun ImageRequest.Builder.() {
                            transformations(
                                CircleCropTransformation(),
                            )
                            crossfade(1000)
                            scale(scale = Scale.FIT)
                        }).build(),
                    //                    error = painterResource(id = R.drawable.google_icon),
                    //                    placeholder = painterResource(id = R.drawable.google_icon)
                )

            Image(
                painter = painter,
                contentDescription = "image",
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(width = 320.dp, height = 320.dp)
                                        .padding(top = 30.dp, bottom = 30.dp),
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 30.dp),
                    text = stringResource(id = R.string.disease_detection)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    Button(
                        onClick = { /*TODO*/ },
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 5.dp
                        ),
                    ) {
                        Row {
                            Icon(
                                modifier = Modifier
                                    .height(20.dp)
                                    .padding(end = 10.dp),
                                imageVector = Icons.Default.Upload,
                                contentDescription = "Upload an image",
                            )
                            Text(text = stringResource(id = R.string.upload_image))

                        }
                    }


                    Button(
                        onClick = { /*TODO*/ },
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 5.dp
                        ),
                    ) {
                        Row {
                            Icon(
                                modifier = Modifier
                                    .height(20.dp)
                                    .padding(end = 10.dp),
                                imageVector = Icons.Default.Camera,
                                contentDescription = "Leading Google Icon",
                            )
                            Text(text = stringResource(id = R.string.take_a_photo))

                        }
                    }
                }
            }
        }
    }
}







