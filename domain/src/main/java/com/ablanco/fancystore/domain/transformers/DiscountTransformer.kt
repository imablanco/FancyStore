package com.ablanco.fancystore.domain.transformers

import com.ablanco.fancystore.domain.models.Discount
import com.ablanco.fancystore.domain.models.ItemsPromoDiscount
import com.ablanco.fancystore.domain.models.Product
import kotlin.math.floor

interface DiscountTransformer<T : Discount> {

    fun applyDiscounts(products: List<Product>, discount: T): List<Product>
}

class ItemsPromoDiscountTransformer : DiscountTransformer<ItemsPromoDiscount> {

    override fun applyDiscounts(
        products: List<Product>,
        discount: ItemsPromoDiscount
    ): List<Product> {
        /* 1 - Group all products by its code to easily operate on each list
        *  2 - Discard products not present in discount
        *  3 - Discard products that do not meet minAmount requirements
        *  4 - Compute how much items are affected by the discount
        *  5 - Recalculate its price based on the discount*/
        return products
            .groupBy { it.code }
            .mapValues { e ->
                if (e.key !in discount.items) return@mapValues e.value
                if (e.value.size < discount.minAmount) return@mapValues e.value

                val amountFactor = discount.amountFactor
                val discountItemCount = floor(e.value.size * amountFactor).toInt()
                val newPriceFactor = (1 - discount.priceFactor)
                val discountItems = e.value
                    .take(discountItemCount)
                    .map { it.copy(price = newPriceFactor * it.price) }
                val noDiscountItems = e.value.drop(discountItemCount)

                discountItems + noDiscountItems
            }.values.flatten()
    }
}
