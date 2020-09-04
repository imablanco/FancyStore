package com.ablanco.fancystore.features.products.presentation

import androidx.annotation.DrawableRes

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */

data class ProductVM(
    val id: String,
    val name: String,
    @DrawableRes val iconResId: Int,
    val price: String,
    val discount: DiscountVM? = null
)

data class DiscountVM(
    val displayName: String,
    val description: String
)

val ProductVM.hasDiscount: Boolean get() = discount != null

