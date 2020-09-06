package com.ablanco.fancystore.domain.models

/**
 * Created by Álvaro Blanco Cabrero on 06/09/2020.
 * FancyStore.
 */
data class Cart(
    val products: List<CartProduct>,
    val total: Double,
    val subtotal: Double,
    val appliedDiscounts: List<Discount>
)
