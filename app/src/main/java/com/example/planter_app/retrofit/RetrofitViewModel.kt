package com.example.planter_app.retrofit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planter_app.BuildConfig
import com.example.planter_app.retrofit.plant_net_api.PlantNetRetrofitInstance
import com.example.planter_app.retrofit.plant_net_api.Root
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File

class RetrofitViewModel() : ViewModel() {
    private val apiKey = BuildConfig.plantNetApikey

    fun plantNetUploadImage(filePath: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val file = File(filePath)
            try {
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val image = MultipartBody.Part.createFormData("images", file.name, requestFile)

                val response: Response<Root> = PlantNetRetrofitInstance.create(apiKey).uploadImage(
                    apiKey = apiKey,
                    images = listOf(image),
                    organs = listOf()
                )

                if (response.isSuccessful) {
                    val root = response.body()
                    if (root != null) {
                        val result = root.results.getOrNull(0) // 0th index has the highest accuracy/score
                        val commonNamesList = result?.species?.commonNames ?: emptyList()
                        val scientificNameWithoutAuthor = result?.species?.family?.scientificNameWithoutAuthor?: ""

                        val returnResponse = if (commonNamesList.isNotEmpty()) {
                            commonNamesList[0]
                        } else if (scientificNameWithoutAuthor.isNotBlank()) {
                            scientificNameWithoutAuthor
                        } else {
                            "No Results found"
                        }
                        onSuccess(returnResponse)

                    } else {
                        onError("Empty response body")
                    }
                } else {
                    onError(response.code().toString())
                }
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error")
            } finally {

            }
        }
    }


    fun MLModelUploadImage(){
        // once the api is hosted...
    }

}

