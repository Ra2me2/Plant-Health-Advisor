package com.example.planter_app.ui.screens.my_plants

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.example.planter_app.R
import com.example.planter_app.firebase_realtime_database.Plant
import com.example.planter_app.firebase_realtime_database.RealTimeDBViewModel
import com.example.planter_app.room_database.PlantTable
import com.example.planter_app.room_database.RoomViewModel
import com.example.planter_app.ui.appbar_and_navigation_drawer.AppBar
import com.example.planter_app.ui.screens.my_plants.plant_details.PlantDetails
import com.example.planter_app.ui.screens.settings.SettingsViewModel
import com.example.planter_app.ui.theme.Planter_appTheme

object MyPlantsScreen : Screen {

    @Composable
    override fun Content() {
        SettingsViewModel.appBarTitle.value = stringResource(id = R.string.MY_PLANTS_SCREEN_TITLE)
        val navigator = LocalNavigator.currentOrThrow

        val realTimeDBViewModel = viewModel<RealTimeDBViewModel>()
        val settingsViewModel = viewModel<SettingsViewModel>()
        val myPlantsViewModel = viewModel<MyPlantsViewModel>()

        val plantsFromFirebase by myPlantsViewModel.plantsList.collectAsStateWithLifecycle()

        settingsViewModel.updateConnectionStatus()
        if (SettingsViewModel.isNetworkAvailable.value){
            LaunchedEffect(Unit) {
                realTimeDBViewModel.fetchPlantData(
                    onSuccess = { plants ->
                        myPlantsViewModel.setPlantsList(plants)
                    },
                    onFailure = {
                        Log.i("TAG", "MyPlantsScreen: error: $it")
                    }
                )
            }

            if (plantsFromFirebase == null){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    CircularProgressIndicator()
                }

            }else{
                if (plantsFromFirebase!!.isNotEmpty()){
                    MyPlantsScreenContent(
                        paddingValues = PaddingValues(top = 0.dp),
                        onClickCard = {img,result,advice,key->
                            navigator.push(
                                PlantDetails(
                                    uri = img,
                                    plantKey = key,
                                    result = result,
                                    advice = advice
                                )
                            )
                        },
                        plantList = plantsFromFirebase!!.toList()
                    )
                }else{
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        Text(text = "No plants", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }else{
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                Text(text = stringResource(id = R.string.no_internet), color = MaterialTheme.colorScheme.error)
            }
        }

    }
}


@Composable
fun MyPlantsScreenContent(
    comingFromPreview: Boolean? = false,
    paddingValues: PaddingValues,
    onClickCard: (String, String, String, String) -> Unit,
    plantList: List<Plant>,
    ) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.leaf),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            alpha = 0.3f
        )
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item{
            plantList.forEach { plant->
                PlantsCard(
                    image = plant.image,
                    result = plant.disease,
                    advice = plant.treatment,
                    key = plant.key,
                    date = plant.date,
                    comingFromPreview = comingFromPreview!!,
                    onClickCard
                )
            }
        }
    }
}

@Composable
fun PlantsCard(
    image: String,
    result: String,
    advice: String,
    key: String,
    date: String,
    comingFromPreview: Boolean,
    onClickCard: (String,String,String,String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        onClick = {
            onClickCard(image,result,advice,key)
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            val painter = if (!comingFromPreview) {
                rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = image)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(500)
                            scale(scale = Scale.FIT)
                        }).build(),
                    error = painterResource(R.drawable.image_not_available),
                    placeholder = painterResource(R.drawable.image_not_available)
                )
            } else {
                painterResource(id = R.drawable.tree_robots_11)
            }
            Image(
                painter = painter,
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
            )
            Column {
                Text(
                    text = result,
                    modifier = Modifier.padding(10.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                )

                Text(
                    text = advice,
                    modifier = Modifier.padding(10.dp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        fontSize = 16.sp,
                    )
                )

                Text(
                    text = date,
                    modifier = Modifier.padding(10.dp),
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(0.5f)
                    ),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun MyPlantsScreenPreview() {
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
                        titleComingFromPreviews = stringResource(id = R.string.MY_PLANTS_SCREEN_TITLE)
                    )
                }
            ) { paddingValues ->
                MyPlantsScreenContent(
                    comingFromPreview = true,
                paddingValues=PaddingValues(),
                onClickCard= {x,y,z,b->},
                plantList =  listOf<Plant>(),
                )
            }
        }
    }
}