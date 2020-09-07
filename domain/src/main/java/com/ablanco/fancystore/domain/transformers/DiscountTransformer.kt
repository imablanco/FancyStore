package com.ablanco.fancystore.domain.transformers

import com.ablanco.fancystore.domain.models.BulkDiscount
import com.ablanco.fancystore.domain.models.CartProduct
import com.ablanco.fancystore.domain.models.Discount
import com.ablanco.fancystore.domain.models.FreeItemDiscount
import kotlin.math.floor

/**
 * Created by Álvaro Blanco Cabrero on 06/09/2020.
 * FancyStore.
 *
 * [DiscountTransformer] is just a generic class that transforms a list of [CartProduct] given
 * a [Discount] applying its business rules
 */
interface DiscountTransformer<T : Discount> {

    fun applyDiscount(products: List<CartProduct>, discount: Discount): List<CartProduct>
}

/**
 * Base implementation of [DiscountTransformer] that performs common business logic for all
 * [DiscountTransformer] implementations
 */
abstract class BaseDiscountTransformer<T : Discount> : DiscountTransformer<T> {

    protected abstract val validator: DiscountValidator<T>

    /*Child classes must perform the real transformation here.
    * ONLY called if the given discount is valid for this products
    * It is guaranteed that the given list contains products with the same id*/
    abstract fun apply(products: List<CartProduct>, discount: T): List<CartProduct>

    @Suppress("UNCHECKED_CAST")
    final override fun applyDiscount(
        products: List<CartProduct>,
        discount: Discount
    ): List<CartProduct> {
        /* 1 - Group all products by code to easily operate on each list
        *  2 - Discard products that do not meet validator requirements
        *  3 - Delegate transformation to subclass*/
        val checkedDiscount = ensureDiscount<T>(discount)
        return products
            .groupBy { it.code }
            .mapValues { (_, group) ->
                if (!validator.isValid(group, checkedDiscount)) return@mapValues group
                apply(group, checkedDiscount)
            }.values.flatten()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> ensureDiscount(discount: Discount): T = discount as? T
        ?: throw IllegalArgumentException("Invalid type ${discount.javaClass.name} passed to this transformer")
}

/**
 * [DiscountTransformer] implementation for [FreeItemDiscount] type
 */
class FreeItemDiscountTransformer : BaseDiscountTransformer<FreeItemDiscount>() {

    override val validator = FreeItemDiscountValidator()

    override fun apply(products: List<CartProduct>, discount: FreeItemDiscount): List<CartProduct> {
        /* Compute how much free items are affected by the discount and change its final price to 0*/
        val minAmount = discount.minAmount
        val freeItems = discount.freeAmount
        val discountItemCount = (floor((products.size / minAmount).toDouble()) * freeItems).toInt()
        val discountItems = products.take(discountItemCount).map { it.copy(finalPrice = 0.0) }
        val noDiscountItems = products.drop(discountItemCount)

        return discountItems + noDiscountItems
    }
}

/**
 * [DiscountTransformer] implementation for [BulkDiscount] type
 */
class BulkDiscountTransformer : BaseDiscountTransformer<BulkDiscount>() {

    override val validator = BulkDiscountValidator()

    override fun apply(products: List<CartProduct>, discount: BulkDiscount): List<CartProduct> {
        /*As this discount affect all items, just compute the final price*/
        val newPriceFactor = (1 - discount.priceFactor)
        return products.map { it.copy(finalPrice = newPriceFactor * it.originalPrice) }
    }
}
