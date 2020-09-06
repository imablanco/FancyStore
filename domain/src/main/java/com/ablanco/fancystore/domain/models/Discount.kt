package com.ablanco.fancystore.domain.models

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */
sealed class Discount {
    abstract val items: List<String>
}

/**
 * Generic way of representing discount promotions such as MxN, X% off buying N+.
 * This way we can group free items promotions and bulk discount promotions by using the same model.
 *
 * i.e: 2x1 promotions could be modeled like:
 * minAmount = 2
 * amountFactor = 0.5 (half of the products affected)
 * priceFactor = 1 (whole price)
 *
 * i.e: Buying 3 or more, get a 5% off on all the products could be modeled like:
 * minAmount = 3
 * amountFactor = 1 (all the items)
 * priceFactor = 0.05 (5% price reduction)
 */
data class ItemsPromoDiscount(
    override val items: List<String>,
    val minAmount: Int,
    val amountFactor: Double,
    val priceFactor: Double
) : Discount()
