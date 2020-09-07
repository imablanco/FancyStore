package com.ablanco.fancystore.domain.models

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */
sealed class Discount {
    abstract val items: List<String>
}

data class FreeItemDiscount(
    override val items: List<String>,
    val minAmount: Int,
    val freeAmount: Int
) : Discount()

data class BulkDiscount(
    override val items: List<String>,
    val minAmount: Int,
    val priceFactor: Double
) : Discount()
