package com.example.planter_app.screens.my_plants.plant_details

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.popUntil
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale

@OptIn(ExperimentalFoundationApi::class)
data class PlantImagesDisplay(val uri: List<String>) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0.1f
        ) { uri.size }

        val colorMatrix by remember { mutableStateOf( ColorMatrix()) }

        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    navigator.popUntil { screen ->
                        if (screen is PlantDetails) {
                            screen.uri == uri
                        } else {
                            false
                        }
                    }
                },
            state = pagerState,
            verticalAlignment = Alignment.CenterVertically
        ) { index ->

            val pageOffSet =
                (pagerState.currentPage - index) + pagerState.currentPageOffsetFraction
            val imageSize by animateFloatAsState(
                targetValue = if (pageOffSet != 0.0f) 0.90f else 1f,
                animationSpec = tween(300), label = ""
            )

//            LaunchedEffect(key1 = imageSize) {
//                if (pageOffSet != 0.0f) {
//                    colorMatrix.setToSaturation(0f)
//                } else {
//                    colorMatrix.setToSaturation(1f)
//                }
//            }

            val painter =
                rememberAsyncImagePainter(

                    ImageRequest.Builder(LocalContext.current)
                        .data(uri[index])
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
                    .height(400.dp)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .graphicsLayer {
                        scaleX = imageSize
                        scaleY = imageSize
                        shape = RoundedCornerShape(20.dp)
                        clip = true
                    },
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.colorMatrix(colorMatrix),
            )
        }
    }
}