package com.ablanco.fancystore.data.network

import com.ablanco.fancystore.data.models.DiscountData
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Álvaro Blanco Cabrero on 03/09/2020.
 * FancyStore.
 */
class ServiceBuilder(private val apiBaseUrl: String) {

    val retrofit: Retrofit by lazy {

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()

        val gson = GsonBuilder()
            .registerTypeAdapter(DiscountData::class.java, ProductDiscountTypeAdapter())
            .create()

        Retrofit.Builder()
            .baseUrl(apiBaseUrl)
            .client(httpClient)
            .addCallAdapterFactory(EitherCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    inline fun <reified T> create(): T = retrofit.create(T::class.java)
}
