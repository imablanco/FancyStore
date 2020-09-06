package com.ablanco.fancystore.domain.transformers

import com.ablanco.fancystore.domain.models.CartProduct
import com.ablanco.fancystore.domain.models.Discount
import com.ablanco.fancystore.domain.models.ItemsPromoDiscount

/**
 * Created by Álvaro Blanco Cabrero on 06/09/2020.
 * FancyStore.
 *
 * Validates if the [Discount] applies to ANY of the given list of [CartProduct]
 */
interface DiscountValidator<T : Discount> {

    fun isValid(products: List<CartProduct>, discount: Discount): Boolean
}

/**
 * Base implementation of [BaseDiscountValidator] that performs common business logic for all
 * [BaseDiscountValidator] implementations
 */
abstract class BaseDiscountValidator<T : Discount> : DiscountValidator<T> {

    /*Child classes must perform the real validation here.
   * ONLY called if the given discount meet common conditions*/
    abstract fun apply(products: List<CartProduct>, discount: T): Boolean

    final override fun isValid(products: List<CartProduct>, discount: Discount): Boolean {
        val checkedDiscount = ensureDiscount<T>(discount)
        /* 1 - Group all products by code to easily operate on each list
        *  2 - Check if discount applies to product group code and specific validations to subclass*/
        return products
            .groupBy(CartProduct::code)
            .any { it.key in discount.items && apply(it.value, checkedDiscount) }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> ensureDiscount(discount: Discount): T = discount as? T
        ?: throw IllegalArgumentException("Invalid type ${discount.javaClass.name} passed to this validator")
}

class ItemsPromoDiscountValidator : BaseDiscountValidator<ItemsPromoDiscount>() {

    /*Only valid if product group size is equal or higher that the min amount required*/
    override fun apply(products: List<CartProduct>, discount: ItemsPromoDiscount): Boolean =
        products.size >= discount.minAmount
}
