package com.ablanco.fancystore.data.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */

/**
 * By keeping [DiscountData] as a generic class for all possible discounts we can easily add more
 * discount types in the future
 */
sealed class DiscountData {
    abstract val items: List<String>
}

data class FreeItemDiscountData(
    @SerializedName("items") override val items: List<String>,
    @SerializedName("minAmount") val minAmount: Int,
    @SerializedName("freeAmount") val freeAmount: Int
) : DiscountData()

data class BulkDiscountData(
    @SerializedName("items") override val items: List<String>,
    @SerializedName("minAmount") val minAmount: Int,
    @SerializedName("priceFactor") val priceFactor: Double
) : DiscountData()
