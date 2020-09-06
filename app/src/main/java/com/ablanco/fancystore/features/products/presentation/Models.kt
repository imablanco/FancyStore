package com.ablanco.fancystore.features.products.presentation

import androidx.annotation.DrawableRes
import com.ablanco.fancystore.utils.presentation.DiscountVM

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

val ProductVM.hasDiscount: Boolean get() = discount != null

