package com.ablanco.fancystore.data.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Álvaro Blanco Cabrero on 03/09/2020.
 * FancyStore.
 */
data class ProductData(
    @SerializedName("code") val code: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("price") val price: Double?
)
