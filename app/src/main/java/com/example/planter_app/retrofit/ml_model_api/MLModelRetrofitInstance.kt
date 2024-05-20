package com.example.planter_app.retrofit.ml_model_api

import com.example.planter_app.retrofit.plant_net_api.PlantNetApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MLModelRetrofitInstance {
    private const val BASE_URL = ""

    fun create(apiKey: String): PlantNetApiService {
        val httpClient = OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("X-Api-Key", apiKey)
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }.build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create(PlantNetApiService::class.java)
    }
}
