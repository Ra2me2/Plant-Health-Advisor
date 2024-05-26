package com.example.planter_app.utilities

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.planter_app.R
import com.example.planter_app.firebase_realtime_database.RealTimeDBViewModel
import com.example.planter_app.retrofit.RetrofitViewModel
import com.example.planter_app.room_database.PlantTable
import com.example.planter_app.room_database.RoomViewModel

@Composable
fun CallAPI(
    photoFilePathUri: String,
//    onSuccessPlantNet: (String) -> Unit,
//    onErrorPlantNet: (String) -> Unit,
    onSuccessModelResult: (String) -> Unit,
    onSuccessModelAdvice: (String) -> Unit,
    onErrorModel: (String) -> Unit
) {
    val retrofitViewModel = viewModel<RetrofitViewModel>()
    val loading = remember{ mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
//        retrofitViewModel.plantNetUploadImage(
//            photoFilePathUri,
//            onSuccess = {
//                loading.value = false
//                onSuccessPlantNet(it)
//            },
//            onError = {
//                onErrorPlantNet(it)
//                loading.value = false
//            }
//        )

        retrofitViewModel.mLModelUploadImage(
            photoFilePathUri,
            onSuccessResult = {
                loading.value = false
                onSuccessModelResult(it)
            },
            onSuccessAdvice = {
                loading.value = false
                onSuccessModelAdvice(it)
            },
            onError = {
                onErrorModel(it)
                loading.value = false
            }
        )

        if (loading.value) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun SaveToFirebaseRealtimeDB(
    context: Context,
    advice: String,
    result: String,
    date:String,
    uri: String
) {
    val realTimeDBViewModel = viewModel<RealTimeDBViewModel>()

    realTimeDBViewModel.savePlantData(
        uri,
        result,
        advice,
        date,
        onSuccess = { message ->
            showToast(context,message)
        },
        onFailure = { exception ->
            showToast(context,"Failed to save plant: ${exception.message}")
        }
    )
}

//@Composable
//fun SaveToRoomDB(
//    result: String,
//    advice: String
//) {
//    val roomViewModel = viewModel<RoomViewModel>()
//
//    val plantTable = PlantTable(
//        id = 0,
//        result = result,
//        advice = advice
//    )
//
//    roomViewModel.upsertData(plantTable)
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorBottomSheet(
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