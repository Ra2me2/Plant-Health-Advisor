package com.example.planter_app.utilities

import android.content.ContentValues.TAG
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import cafe.adriel.voyager.navigator.Navigator
import com.example.planter_app.MyApplication
import com.example.planter_app.ui.screens.my_plants.plant_details.PlantDetails
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun singlePhotoPickerFromGallery(
    navigator: Navigator
): ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> {
    val context = LocalContext.current
    val photoFilePathUri = remember { mutableStateOf<String?>(null) }

    val modelResult = remember { mutableStateOf<String?>(null) }
    val modelAdvice = remember { mutableStateOf<String?>(null) }
    val modelError = remember { mutableStateOf<String?>(null) }
    val plantNetResult = remember { mutableStateOf<String?>(null) }
    val plantNetError = remember { mutableStateOf<String?>(null) }


    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                photoFilePathUri.value = getRealPathFromURI(uri)
            }
        }
    )

    if (photoFilePathUri.value != null) {
        CallAPI(photoFilePathUri = photoFilePathUri.value!!,
//            onSuccessPlantNet = {
//                plantNetResult.value = it
//            },
//            onErrorPlantNet = {
//                plantNetError.value = it
//            },
            onSuccessModelResult = {
                modelResult.value = it
            },
            onSuccessModelAdvice = {
                modelAdvice.value = it
            },
            onErrorModel = {
                modelError.value = it
            }
        )
    }


    if (!modelResult.value.isNullOrBlank() && !modelAdvice.value.isNullOrBlank()) {

        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        SaveToFirebaseRealtimeDB(
            context = context,
            uri = photoFilePathUri.value!!,
            result = modelResult.value!!,
            advice = modelAdvice.value!!,
            date = currentDate
        )

        navigator.push(
            PlantDetails(
                uri = photoFilePathUri.value!!,
                result = modelResult.value,
                advice = modelAdvice.value
            )
        )
    }

    return singlePhotoPicker
}

fun getRealPathFromURI(uri: Uri): String? {
    val context = MyApplication.instance!!.applicationContext
    val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
    return cursor?.use {
        it.moveToFirst()
        val columnIndex: Int = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        val filePath: String = it.getString(columnIndex)
        it.close()
        filePath
    }
}
