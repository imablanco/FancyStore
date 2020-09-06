package com.ablanco.fancystore.data.models

import com.ablanco.fancystore.domain.models.*
import com.ablanco.fancystore.domain.transformers.DiscountTransformers
import com.ablanco.fancystore.domain.transformers.DiscountValidators

/**
 * Created by Álvaro Blanco Cabrero on 03/09/2020.
 * FancyStore.
 */

fun ProductListData.toDomain(): List<Product> =
    products.orEmpty().map(ProductData::toDomain)

fun ProductData.toDomain(): Product =
    Product(
        code = code.orEmpty(),
        name = name.orEmpty(),
        price = price ?: 0.0
    )

fun DiscountData.toDomain(): Discount = when (this) {
    is ItemsPromoDiscountData -> ItemsPromoDiscount(
        items = items,
        minAmount = minAmount,
        amountFactor = amountFactor,
        priceFactor = priceFactor
    )
}

fun Product.toCartProduct(): CartProduct =
    CartProduct(
        code = code,
        name = name,
        originalPrice = price,
        finalPrice = price
    )

/**
 * Maps a list of [Product] to a [Cart] instance by applying the given list of [Discount]
 */
class CartMapper(
    private val discountTransformers: DiscountTransformers,
    private val discountValidators: DiscountValidators
) {

    fun map(products: List<CartProduct>, discounts: List<Discount>): Cart {
        val newProducts = discountTransformers.applyDiscounts(products, discounts)
        val validators = discountValidators.validators
        val appliedDiscounts = discounts.filter { d ->
            validators.any { it.isValid(products, d) }
        }
        val total = newProducts.sumByDouble { it.finalPrice }
        val subtotal = products.sumByDouble { it.originalPrice }
        return Cart(
            products = newProducts,
            total = total,
            subtotal = subtotal,
            appliedDiscounts = appliedDiscounts
        )
    }
}
