package com.ablanco.fancystore.data.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Álvaro Blanco Cabrero on 03/09/2020.
 * FancyStore.
 */
data class ProductListData(
    @SerializedName("products") val products: List<ProductData>?
)
