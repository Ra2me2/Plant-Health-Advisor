package com.example.planter_app.image_capture_n_picker

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import cafe.adriel.voyager.navigator.Navigator
import com.example.planter_app.screens.my_plants.plant_details.PlantDetails
import java.io.File
import java.util.Objects

@Composable
fun singlePhotoPickerFromGallery(
    navigator: Navigator
): ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> {
    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri!=null) {
                navigator.push(PlantDetails( uri.toString() ))
            }
        }
    )
    return singlePhotoPicker
}

@Composable
fun captureImageFromCamera(navigator: Navigator): Triple<Uri, ManagedActivityResultLauncher<String, Boolean>, ManagedActivityResultLauncher<Uri, Boolean>> {
    val context = LocalContext.current
    val file = File.createTempFile(
        "imageFileName", // prefix
        ".jpg", // suffix
        context.externalCacheDir  // directory
    )
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture())
        { isTaken ->
            if (isTaken) {
                navigator.push(PlantDetails( uri.toString() ))
            }
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { permissionGranted->
            if (permissionGranted) {
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                cameraLauncher.launch(uri)
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    return Triple(uri, permissionLauncher, cameraLauncher)
}
