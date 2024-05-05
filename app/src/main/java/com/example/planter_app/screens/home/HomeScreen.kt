package com.example.planter_app.screens.home

import android.content.ContentValues.TAG
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import android.widget.Space
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.planter_app.R
import com.example.planter_app.firebase_login.sign_in.SignInScreen
import com.example.planter_app.navigation_drawer.AppBar
import com.example.planter_app.navigation_drawer.NavigationDrawer
import com.example.planter_app.screens.my_plants.plant_details.PlantDetails
import com.example.planter_app.screens.settings.SettingsViewModel
import com.example.planter_app.ui.theme.Planter_appTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.coroutines.Continuation

object HomeScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        SettingsViewModel.appBarTitle.value = stringResource(id = R.string.HOME_SCREEN_TITLE)

        val navigator = LocalNavigator.currentOrThrow
        val homeViewModel = viewModel<HomeViewModel>()
        val settingsViewModel = viewModel<SettingsViewModel>()
        val imageUri = homeViewModel.imageUri.collectAsState()

        val isNetworkAvailable = settingsViewModel.isNetworkAvailable.collectAsStateWithLifecycle()

        val (currentImageIndex, multiplePhotoPicker, imageList) = multiplePhotoPicker(
            homeViewModel = homeViewModel,
            navigator = navigator
        )


        val pullToRefreshState = rememberPullToRefreshState()
        val isRefreshing by settingsViewModel.isRefreshing.collectAsStateWithLifecycle()

        Box(
            modifier = Modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)
        )
        {
            HomeScreenContent(
                imageList[currentImageIndex],
                updateConnectionStatus = {
                    settingsViewModel.updateConnectionStatus()
                },
                isNetworkAvailable,
                multiplePhotoPickerLaunch = {
                    multiplePhotoPicker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            )

            if (pullToRefreshState.isRefreshing) {
                LaunchedEffect(true) {
                    settingsViewModel.updateRefresh()
                }
            }

            LaunchedEffect(isRefreshing) {
                if (isRefreshing) {
                    pullToRefreshState.startRefresh()
                } else {
                    pullToRefreshState.endRefresh()
                }
            }

            PullToRefreshContainer(
                state = pullToRefreshState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )

        }
    }
}

@Composable
fun HomeScreenContent(
    image: Int? = null,
    updateConnectionStatus: () -> Unit,
    isNetworkAvailable: State<Boolean>,
    multiplePhotoPickerLaunch: () -> Unit,
    paddingValuesfromPreview: PaddingValues? = null,

    ) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        horizontalAlignment = Alignment.Start
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValuesfromPreview?.calculateTopPadding() ?: 0.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (image != null) {
                    val painter =
                        rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = image)
                                .apply(block = fun ImageRequest.Builder.() {
                                    transformations(
                                        CircleCropTransformation(),
                                    )
                                    crossfade(1000)
                                    scale(scale = Scale.FIT)
                                }).build(),
                            //                    error = painterResource(),
                            //                    placeholder = painterResource()
                        )

                    Image(
                        painter = painter,
                        contentDescription = "image",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(width = 400.dp, height = 400.dp)
                            .padding(top = 30.dp, bottom = 30.dp),
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.tree_robots_1),
                        contentDescription = "image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.padding(bottom = 30.dp))
                }


            }


            Text(
                modifier = Modifier.padding(bottom = 30.dp),
                color = MaterialTheme.colorScheme.secondary,
                text = stringResource(id = R.string.disease_detection)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Button(
                    onClick = {
                        updateConnectionStatus()
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(100)
                            if (isNetworkAvailable.value) {
                                multiplePhotoPickerLaunch()
                            }
                        }
                    },
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 5.dp
                    ),
                    enabled = isNetworkAvailable.value
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
                    onClick = {
                        updateConnectionStatus()
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(100)

                            if (isNetworkAvailable.value) {
                                /*TODO*/

                            }
                        }

                    },
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 5.dp
                    ),
                    enabled = isNetworkAvailable.value
                ) {
                    Row {
                        Icon(
                            modifier = Modifier
                                .height(20.dp)
                                .padding(end = 10.dp),
                            imageVector = Icons.Default.Camera,
                            contentDescription = "",
                        )
                        Text(text = stringResource(id = R.string.take_a_photo))

                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 15.dp)
            ) {
                if (!isNetworkAvailable.value) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error,
                        text = stringResource(id = R.string.no_internet)
                    )
                }
            }
        }
    }
}

@Composable
fun multiplePhotoPicker(
    homeViewModel: HomeViewModel,
    navigator: Navigator
): Triple<Int, ManagedActivityResultLauncher<PickVisualMediaRequest, List<@JvmSuppressWildcards Uri>>, List<Int>> {
    val multiplePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 3),
        onResult = { uri ->
            homeViewModel.setImageUri(uri)
            val uriStrings = uri.map { it.toString() }
            if (uri.isNotEmpty()) {
                navigator.push(PlantDetails(uriStrings))
            }
        }
    )

    val imageList = listOf(
        R.drawable.tree_robots_1,
        R.drawable.tree_robots_2,
        R.drawable.tree_robots_3,
        R.drawable.tree_robots_4,
        R.drawable.tree_robots_5,
        R.drawable.tree_robots_6,
        R.drawable.tree_robots_7,
        R.drawable.tree_robots_8,
        R.drawable.tree_robots_9,
        R.drawable.tree_robots_10,
        R.drawable.tree_robots_11,
        R.drawable.tree_robots_12,
        R.drawable.tree_robots_13,
        R.drawable.tree_robots_14,
        R.drawable.tree_robots_15,
        R.drawable.tree_robots_16,
        R.drawable.tree_robots_17,
        R.drawable.tree_robots_18
    )

    val calendar = Calendar.getInstance()
    val currentDayOfYear = calendar.get(Calendar.DAY_OF_YEAR)

    Log.i(TAG, "Content: date is $currentDayOfYear")

    var currentImageIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = currentImageIndex) {
        while (true) {
            delay(30_000) // will change to 1 day later...
            currentImageIndex = (currentImageIndex + 1) % imageList.size
        }
    }
    return Triple(currentImageIndex, multiplePhotoPicker, imageList)
}


@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun HomeScreenPreview() {
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
                        titleComingFromPreviews = stringResource(id = R.string.HOME_SCREEN_TITLE)
                    )
                }
            ) { paddingValues ->
                HomeScreenContent(
                    paddingValuesfromPreview = paddingValues,
                    updateConnectionStatus = { },
                    isNetworkAvailable = remember { mutableStateOf(true) },
                    multiplePhotoPickerLaunch = {}
                )
            }
        }
    }
}


