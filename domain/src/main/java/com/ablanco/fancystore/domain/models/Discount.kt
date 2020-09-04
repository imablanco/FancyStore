package com.ablanco.fancystore.domain.models

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */
sealed class Discount

data class FreeItemPromotion(
    val minAmount: Int,
    val freeAmount: Int
) : Discount()

data class BulkDiscount(
    val minAmount: Int,
    val discountFactor: Double
) : Discount()
