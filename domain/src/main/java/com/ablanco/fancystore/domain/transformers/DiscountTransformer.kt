package com.ablanco.fancystore.domain.transformers

import com.ablanco.fancystore.domain.models.CartProduct
import com.ablanco.fancystore.domain.models.Discount
import com.ablanco.fancystore.domain.models.ItemsPromoDiscount
import kotlin.math.floor
import kotlin.reflect.KClass

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
    * ONLY called if the given discount is valid for this products*/
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

class ItemsPromoDiscountTransformer : BaseDiscountTransformer<ItemsPromoDiscount>() {

    override val validator: DiscountValidator<ItemsPromoDiscount> = ItemsPromoDiscountValidator()

    override fun apply(
        products: List<CartProduct>,
        discount: ItemsPromoDiscount
    ): List<CartProduct> {
        /* 1 - Compute how much items are affected by the discount
        *  2 - Recalculate its price based on the discount*/
        val amountFactor = discount.amountFactor
        val discountItemCount = floor(products.size * amountFactor).toInt()
        val newPriceFactor = (1 - discount.priceFactor)
        val discountItems = products
            .take(discountItemCount)
            .map { it.copy(finalPrice = newPriceFactor * it.originalPrice) }
        val noDiscountItems = products.drop(discountItemCount)

        return discountItems + noDiscountItems
    }
}
