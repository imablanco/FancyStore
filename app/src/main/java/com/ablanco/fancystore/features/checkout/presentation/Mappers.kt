package com.ablanco.fancystore.features.checkout.presentation

import com.ablanco.fancystore.base.presentation.StringsProvider
import com.ablanco.fancystore.domain.models.Cart
import com.ablanco.fancystore.domain.models.CartProduct
import com.ablanco.fancystore.utils.presentation.formatCurrency
import com.ablanco.fancystore.utils.presentation.iconResId
import com.ablanco.fancystore.utils.presentation.toPresentation

/**
 * Created by Álvaro Blanco Cabrero on 05/09/2020.
 * FancyStore.
 */
class CheckoutPresentationMapper(private val stringsProvider: StringsProvider) {

    fun map(cart: Cart): List<CheckoutItem> {
        if (cart.products.isEmpty()) return emptyList()

        val discounts = cart.appliedDiscounts.map { it.toPresentation(stringsProvider) }
        val products = cart.products.map(CartProduct::toCheckoutProduct)
        val discountTotal = (cart.subtotal - cart.total).takeIf { discounts.isNotEmpty() }
        val resume = CheckoutResume(
            total = cart.total.formatCurrency(),
            subtotal = cart.subtotal.formatCurrency(),
            discount = discountTotal?.let { "-${it.formatCurrency()}" },
            discountBreakdown = buildString {
                discounts.map { appendln("- ${it.description}") }
            }.takeIf { it.isNotEmpty() }
        )
        return products + resume
    }
}

private fun CartProduct.toCheckoutProduct(): CheckoutProduct =
    CheckoutProduct(
        id = code,
        name = name,
        iconResId = iconResId,
        originalPrice = originalPrice.formatCurrency(),
        finalPrice = finalPrice.formatCurrency(),
        hasDiscount = finalPrice < originalPrice
    )
