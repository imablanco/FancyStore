package com.ablanco.fancystore.domain.transformers

import com.ablanco.fancystore.domain.models.CartProduct
import com.ablanco.fancystore.domain.models.Discount
import com.ablanco.fancystore.domain.models.ItemsPromoDiscount

/**
 * Created by Álvaro Blanco Cabrero on 06/09/2020.
 * FancyStore.
 *
 * Validates if the [Discount] applies to given list of [CartProduct]
 */
interface DiscountValidator<T : Discount> {

    fun isValid(products: List<CartProduct>, discount: T): Boolean
}

class ItemsPromoDiscountValidator : DiscountValidator<ItemsPromoDiscount> {

    override fun isValid(products: List<CartProduct>, discount: ItemsPromoDiscount): Boolean =
        products
            .groupBy { it.code }
            .any { it.key in discount.items && it.value.size >= discount.minAmount }
}
