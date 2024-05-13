package com.example.planter_app.screens.my_plants.plant_details

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.example.planter_app.ui.theme.Planter_appTheme

@OptIn(ExperimentalFoundationApi::class)
data class PlantImagesDisplay(val uri: String) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0.1f,
            pageCount = { 1 }
        )

        val colorMatrix by remember { mutableStateOf(ColorMatrix()) }

        PlantImagesDisplayContent(
            onClick = {
                navigator.popUntil { screen ->
                    if (screen is PlantDetails) {
                        screen.uri == uri
                    } else {
                        false
                    }
                }
            },
            pagerState,
            colorMatrix,
            uri
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlantImagesDisplayContent(
    onClick: () -> Unit,
    pagerState: PagerState = rememberPagerState(pageCount = { 1 }), // Default to 1 page
    colorMatrix: ColorMatrix? = null, // Default empty ColorMatrix
    uri: String? = null,
    ) {
    Box(
        modifier = Modifier.fillMaxSize(),

        ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.leaf),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            alpha = 0.3f
        )
    }

        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onClick()
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


            val painter = if (!uri.isNullOrEmpty()) {
                rememberAsyncImagePainter(

                    ImageRequest.Builder(LocalContext.current)
                        .data(uri)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(800)
                            scale(scale = Scale.FIT)
                        }).build(),
                    //                    error = painterResource(),
                    //                    placeholder = painterResource()
                )
            } else {
                painterResource(id = R.drawable.tree_robots_1)
            }


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
                colorFilter = colorMatrix?.let { ColorFilter.colorMatrix(it) },
            )
        }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun PlantImagesDisplay() {
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
                PlantImagesDisplayContent(
                    onClick = { },
                )
            }
        }
    }

}