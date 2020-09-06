package com.ablanco.fancystore.domain.transformers

import com.ablanco.fancystore.domain.models.CartProduct
import com.ablanco.fancystore.domain.models.Discount
import com.ablanco.fancystore.domain.models.ItemsPromoDiscount
import kotlin.reflect.KClass

/**
 * Created by Álvaro Blanco Cabrero on 06/09/2020.
 * FancyStore.
 *
 * Generic way to apply transformations for a dynamic list of [Discount]
 */
interface DiscountTransformers {

    //TODO return also applied discounts to delete validators
    fun applyDiscounts(products: List<CartProduct>, discounts: List<Discount>): List<CartProduct>
}

/**
 * Implementation of [DiscountTransformers] that contains one [DiscountTransformer] for every
 * known [Discount] subclass
 */
class DiscountTransformersImpl : DiscountTransformers {

    private val transformers: Map<KClass<out Discount>, DiscountTransformer<*>> = mapOf(
        ItemsPromoDiscount::class to ItemsPromoDiscountTransformer()
    )

    override fun applyDiscounts(
        products: List<CartProduct>,
        discounts: List<Discount>
    ): List<CartProduct> =
        discounts.foldRight(products) { discount, acc ->
            val transformer = transformers[discount::class]
            transformer?.applyDiscount(acc, discount) ?: acc
        }
}
