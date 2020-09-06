package com.ablanco.fancystore.domain.transformers

import com.ablanco.fancystore.domain.models.Discount
import com.ablanco.fancystore.domain.models.FreeItemDiscount
import com.ablanco.fancystore.domain.models.Product
import kotlin.math.floor

interface DiscountTransformer<T : Discount> {

    fun applyDiscounts(products: List<Product>, discount: T): List<Product>
}

class FreeItemDiscountTransformer : DiscountTransformer<FreeItemDiscount> {

    override fun applyDiscounts(
        products: List<Product>,
        discount: FreeItemDiscount
    ): List<Product> {
        /* 1 - Group all products by its code to easily operate on each list
        *  2 - Discard products not present in discount
        *  3 - Discard products that do not meet minAmount requirements
        *  4 - Compute how much items are affected by the discount
        *  5 - Recalculate its price based on the discount*/
        return products
            .groupBy { it.code }
            .mapValues {
                if (it.key !in discount.items) return@mapValues it.value
                if (it.value.size < discount.minAmount) return@mapValues it.value

                val min = discount.minAmount
                val free = discount.freeAmount
                val freeItems = (floor(it.value.size / min.toDouble()) * free).toInt()
                it.value.take(freeItems).map { it.copy(price = 0.0) } + it.value.drop(freeItems)
            }.values.flatten()
    }
}
