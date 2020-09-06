package com.ablanco.fancystore.domain.models

/**
 * Created by Álvaro Blanco Cabrero on 03/09/2020.
 * FancyStore.
 */
data class CartProduct(
    val code: String,
    val name: String,
    val originalPrice: Double,
    val finalPrice: Double
)
