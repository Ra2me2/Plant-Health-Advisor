package com.example.planter_app.ui.screens.my_plants.plant_details

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.example.planter_app.R
import com.example.planter_app.appbar_and_navigation_drawer.AppBar
import com.example.planter_app.retrofit.RetrofitViewModel
import com.example.planter_app.ui.screens.home.HomeScreen
import com.example.planter_app.ui.screens.my_plants.MyPlantsViewModel
import com.example.planter_app.ui.screens.settings.SettingsViewModel
import com.example.planter_app.ui.theme.Planter_appTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


data class PlantDetails(val uri: Uri, val comingFromMyPlants: Boolean = false) : Screen {

    @Composable
    override fun Content() {

        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow
        val retrofitViewModel = viewModel<RetrofitViewModel>()
        val myPlantsViewModel = viewModel<MyPlantsViewModel>()
        val settingsViewModel = viewModel<SettingsViewModel>()


        val imageClicked = myPlantsViewModel.imageClicked.collectAsStateWithLifecycle()
        val plantNetApiResponse =
            myPlantsViewModel.plantNetApiResponse.collectAsStateWithLifecycle()

        if (imageClicked.value) {
            navigator.push(PlantImagesDisplay(uri.toString()))
            myPlantsViewModel.setImageClicked(false)
        }

        // calling plantNetAPI, getting response
        LaunchedEffect(Unit) {
            retrofitViewModel.plantNetUploadImage(
                uri.toString(),
                onSuccess = {
                    myPlantsViewModel.setPlantNetApiResponse(it)
                },
                onError = {
                    myPlantsViewModel.setPlantNetApiResponse(it)
                }
            )
        }
        if (MyPlantsViewModel.plantDeleteIcon.value) {
            DeletePlant(
                updateConnectionStatus = {
                    settingsViewModel.updateConnectionStatus()
                }
            )
        }

        if (comingFromMyPlants) {
            PlantDetailsContent(
                onImageClick = {
                    myPlantsViewModel.setImageClicked(it)
                },
                context, uri.toString()
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (plantNetApiResponse.value.isNullOrBlank()) {
                    SettingsViewModel.appBarTitle.value =
                        stringResource(id = R.string.LOADING_SCREEN)
                    CircularProgressIndicator()

                } else {
                    if (plantNetApiResponse.value == "404") {
                        BottomSheet(
                            sheetOpen = true,
                            backToHomeScreen = {
                                navigator.replace(HomeScreen)
                            }
                        )
                    } else {
                        PlantDetailsContent(
                            onImageClick = {
                                myPlantsViewModel.setImageClicked(it)
                            },
                            context, uri.toString()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlantDetailsContent(
    onImageClick: (Boolean) -> Unit,
    context: Context,
    uri: String? = null
) {
    SettingsViewModel.appBarTitle.value = stringResource(id = R.string.PLANT_INFO_SCREEN_TITLE)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        val painter = if (!uri.isNullOrEmpty()) {
            rememberAsyncImagePainter(
                ImageRequest.Builder(context)
                    .data(uri)
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
                    onImageClick(true)
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
@Composable
fun BottomSheet(
    sheetOpen: Boolean = false,
    backToHomeScreen: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by remember {
        mutableStateOf(sheetOpen)
    }

    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                isSheetOpen = false
                backToHomeScreen()
            }
        )
        {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.leaf),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    alpha = 0.3f
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.plant_not_detected),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        style = TextStyle.Default.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = stringResource(id = R.string.photo_requirement),
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        style = TextStyle.Default.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Justify,
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeletePlant(
    updateConnectionStatus: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by remember {
        mutableStateOf(MyPlantsViewModel.plantDeleteIcon.value)
    }

    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                isSheetOpen = false
                MyPlantsViewModel.plantDeleteIcon.value = false
            }
        )
        {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.leaf),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    alpha = 0.3f
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Are you sure you want to delete the plant?",
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        style = TextStyle.Default.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(
                            modifier = Modifier.padding(20.dp),
                            onClick = {
                                updateConnectionStatus()
                                CoroutineScope(Dispatchers.Main).launch {
                                    if (SettingsViewModel.isNetworkAvailable.value) {
                                        /* TODO() once firebase database is setup*/
                                    }
                                }
                            },
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 2.dp,
                                pressedElevation = 5.dp
                            ),
                            enabled = SettingsViewModel.isNetworkAvailable.value
                        ) {
                            Text(text = stringResource(id = R.string.yes))
                        }

                        Button(
                            modifier = Modifier.padding(20.dp),
                            onClick = {
                                updateConnectionStatus()
                                CoroutineScope(Dispatchers.Main).launch {
                                    if (SettingsViewModel.isNetworkAvailable.value) {
                                        /* TODO() once firebase database is setup*/
                                    }
                                }
                            },
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 2.dp,
                                pressedElevation = 5.dp
                            ),
                            enabled = SettingsViewModel.isNetworkAvailable.value
                        ) {
                            Text(text = stringResource(id = R.string.no))
                        }
                    }
                    if (!SettingsViewModel.isNetworkAvailable.value) {
                        Text(
                            modifier = Modifier.padding(top = 20.dp),
                            text = stringResource(id = R.string.no_internet),
                            color = MaterialTheme.colorScheme.error
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
                    onImageClick = { },
                    context = LocalContext.current
                )
            }
        }
    }
}