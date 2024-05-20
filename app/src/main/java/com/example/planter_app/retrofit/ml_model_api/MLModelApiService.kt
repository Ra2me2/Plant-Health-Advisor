package com.example.planter_app.retrofit.ml_model_api

import com.example.planter_app.retrofit.plant_net_api.Root
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST

interface MLModelApiService {
    @Multipart
    @POST()
    suspend fun uploadImage(

    ): Response<Root>
}