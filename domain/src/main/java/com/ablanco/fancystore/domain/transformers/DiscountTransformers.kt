package com.ablanco.fancystore.domain.transformers

import com.ablanco.fancystore.domain.models.CartProduct
import com.ablanco.fancystore.domain.models.Discount
import com.ablanco.fancystore.domain.models.ItemsPromoDiscount
import kotlin.reflect.KClass

/**
 * Created by Álvaro Blanco Cabrero on 06/09/2020.
 * FancyStore.
 *
 * Generic way to apply transformations for a dynamic list of [Discount].
 * It returns a [Pair] with the transformed list of [CartProduct] and the applied [Discount] or and
 * empty list if no discounts were applied
 */
interface DiscountTransformers {

    fun applyDiscounts(
        products: List<CartProduct>,
        discounts: List<Discount>
    ): Pair<List<CartProduct>, List<Discount>>
}

/**
 * Implementation of [DiscountTransformers] that contains one [DiscountTransformer] for every
 * known [Discount] subclass
 */
class DiscountTransformersImpl : DiscountTransformers {

    private val transformers: Map<KClass<out Discount>, BaseDiscountTransformer<*>> = mapOf(
        ItemsPromoDiscount::class to ItemsPromoDiscountTransformer()
    )

    override fun applyDiscounts(
        products: List<CartProduct>,
        discounts: List<Discount>
    ): Pair<List<CartProduct>, List<Discount>> =
        /*By folding we want accumulate values over every iteration */
        discounts.foldRight(products to emptyList()) { discount, acc ->
            val transformer = transformers[discount::class] ?: return@foldRight acc

            val accProducts = acc.first
            val accDiscounts = acc.second
            val newProducts = transformer.applyDiscount(accProducts, discount)
            /*By comparing if new products are different we can assume that the discount has
            * been applied*/
            val appliedDiscount = discount.takeIf { !newProducts.isEqualTo(accProducts) }
            newProducts to accDiscounts + listOfNotNull(appliedDiscount)
        }

    private fun <T> List<T>.isEqualTo(other: List<T>) =
        containsAll(other) && other.containsAll(this)
}
