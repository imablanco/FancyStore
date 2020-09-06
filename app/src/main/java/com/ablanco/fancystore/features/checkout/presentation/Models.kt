package com.ablanco.fancystore.features.checkout.presentation

import androidx.annotation.DrawableRes

/**
 * Created by Álvaro Blanco Cabrero on 05/09/2020.
 * FancyStore.
 */

sealed class CheckoutItem

data class CheckoutProduct(
    val id: String,
    val name: String,
    @DrawableRes val iconResId: Int,
    val originalPrice: String?,
    val finalPrice: String,
    val hasDiscount: Boolean
) : CheckoutItem()

data class CheckoutResume(
    val total: String,
    val subtotal: String,
    val discount: String?,
    val discountInfo: String?
) : CheckoutItem()
