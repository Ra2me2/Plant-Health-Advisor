package com.example.planter_app.screens.my_plants.plant_details

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.example.planter_app.R
import com.example.planter_app.screens.home.HomeScreen
import com.example.planter_app.screens.settings.SettingsViewModel


data class PlantDetails(val uri: List<String> = emptyList()) : Screen {

    @Composable
    override fun Content() {
        SettingsViewModel.appBarTitle.value =
            stringResource(id = R.string.PLANTS_DETAILS_SCREEN_TITLE)

        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow

        var imageClicked by remember { mutableStateOf(false) }

        if (imageClicked){
            navigator.push(PlantImagesDisplay(uri))
            imageClicked = false
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            val painter =
                rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data(uri[0])
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(800)
                            scale(scale = Scale.FIT)
                        }).build(),
                    //                    error = painterResource(),
                    //                    placeholder = painterResource()
                )

            Image(
                painter = painter,
                contentDescription = "image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(bottomEnd = 70.dp, topStart = 70.dp))
                    .clickable {
                        imageClicked = true
                    },
                contentScale = ContentScale.FillWidth
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, start = 15.dp, end = 15.dp),
            ) {
                item {
                    Text(
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                        text = "${stringResource(id = R.string.plant_description)}:  ",
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        color = MaterialTheme.colorScheme.primary,
                        style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        modifier = Modifier.padding(bottom = 10.dp),
                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmodtempor incididunt ut labore et dolore magna aliqua. Suspendisse potenti nullam ac tortor vitae. Pulvinar mattis nunc sed blandit liberovolutpat. Duis \n",
                        textAlign = TextAlign.Justify,
                        color = MaterialTheme.colorScheme.secondary,
                        )

                    Row(
                        modifier = Modifier.padding(bottom = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${stringResource(id = R.string.disease_name)}:  ",
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            color = MaterialTheme.colorScheme.primary,
                            style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(text = "Disease Type / Not detected", color = MaterialTheme.colorScheme.secondary)
                    }


                    Text(
                        modifier = Modifier.padding(bottom = 10.dp),
                        text = "${stringResource(id = R.string.treatment)}:  ",
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        color = MaterialTheme.colorScheme.primary,
                        style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                    )

                    Text(
                        modifier = Modifier.padding(bottom = 10.dp),
                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmodtempor incididunt ut labore et dolore magna aliqua. Suspendisse potenti nullam ac tortor vitae. Pulvinar mattis nunc sed blandit liberovolutpat. Duis \n",
                        textAlign = TextAlign.Justify,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    Text(
                        modifier = Modifier.padding(bottom = 10.dp),
                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmodtempor incididunt ut labore et dolore magna aliqua. Suspendisse potenti nullam ac tortor vitae. Pulvinar mattis nunc sed blandit liberovolutpat. Duis \n",
                        textAlign = TextAlign.Justify,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    Text(
                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmodtempor incididunt ut labore et dolore magna aliqua. Suspendisse potenti nullam ac tortor vitae. Pulvinar mattis nunc sed blandit liberovolutpat. Duis \n",
                        textAlign = TextAlign.Justify,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }

            }

        }
    }
}