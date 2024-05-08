package com.example.planter_app.screens.my_plants.plant_details

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.example.planter_app.R
import com.example.planter_app.appbar_and_navigation_drawer.AppBar
import com.example.planter_app.screens.settings.SettingsViewModel
import com.example.planter_app.ui.theme.Planter_appTheme


data class PlantDetails(val uri: List<String> = emptyList()) : Screen {

    @Composable
    override fun Content() {
        SettingsViewModel.appBarTitle.value =
            stringResource(id = R.string.PLANT_INFO_SCREEN_TITLE)

        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow

        val imageClicked = remember { mutableStateOf(false) }

        if (imageClicked.value) {
            navigator.push(PlantImagesDisplay(uri))
            imageClicked.value = false
        }

        PlantDetailsContent(imageClicked, context, uri)
    }
}

@Composable
fun PlantDetailsContent(
    imageClicked: MutableState<Boolean>,
    context: Context,
    uri: List<String>? = null
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        val painter = if (!uri.isNullOrEmpty()) {
            rememberAsyncImagePainter(
                ImageRequest.Builder(context)
                    .data(uri[0])
                    .apply(block = fun ImageRequest.Builder.() {
                        crossfade(800)
                        scale(scale = Scale.FIT)
                    }).build(),
                //                    error = painterResource(),
            )
        } else {
            painterResource(id = R.drawable.tree_robots_1)
        }

        Image(
            painter = painter,
            contentDescription = "image",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(bottomEnd = 70.dp, topStart = 70.dp))
                .clickable {
                    imageClicked.value = true
                },
            contentScale = ContentScale.FillWidth,
        )

        Box(
            modifier = Modifier.fillMaxSize(),
        )
        {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.leaf),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                alpha = 0.3f
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, start = 15.dp, end = 15.dp),
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 5.dp
                        )
                    ) {
                        Text(
                            modifier = Modifier.padding(20.dp),
                            text = "${stringResource(id = R.string.plant_description)}:  ",
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmodtempor incididunt ut labore et dolore magna aliqua. Suspendisse potenti nullam ac tortor vitae. Pulvinar mattis nunc sed blandit liberovolutpat. Duis \n",
                            textAlign = TextAlign.Justify,
                        )

                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 5.dp
                        )
                    ) {
                        Text(
                            modifier = Modifier.padding(20.dp),
                            text = "${stringResource(id = R.string.disease_name)}:  ",
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
                            text = "Disease Type / Not detected",
                        )
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 5.dp
                        )
                    ) {
                        Text(
                            modifier = Modifier.padding(20.dp),
                            text = "${stringResource(id = R.string.treatment)}:  ",
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                        )

                        Text(
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmodtempor incididunt ut labore et dolore magna aliqua. Suspendisse potenti nullam ac tortor vitae. Pulvinar mattis nunc sed blandit liberovolutpat. Duis \n",
                            textAlign = TextAlign.Justify,
                        )
                        Text(
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmodtempor incididunt ut labore et dolore magna aliqua. Suspendisse potenti nullam ac tortor vitae. Pulvinar mattis nunc sed blandit liberovolutpat. Duis \n",
                            textAlign = TextAlign.Justify,
                        )
                        Text(
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmodtempor incididunt ut labore et dolore magna aliqua. Suspendisse potenti nullam ac tortor vitae. Pulvinar mattis nunc sed blandit liberovolutpat. Duis \n",
                            textAlign = TextAlign.Justify,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun PlantDetailsPreview() {
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
                        titleComingFromPreviews = stringResource(id = R.string.PLANT_INFO_SCREEN_TITLE)
                    )
                }
            ) { paddingVales ->
                Spacer(modifier = Modifier.padding(top = paddingVales.calculateTopPadding()))
                PlantDetailsContent(
                    imageClicked = remember { mutableStateOf(false) },
                    context = LocalContext.current
                )
            }
        }
    }
}