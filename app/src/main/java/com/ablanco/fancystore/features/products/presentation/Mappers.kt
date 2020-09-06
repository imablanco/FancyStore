package com.ablanco.fancystore.features.products.presentation

import com.ablanco.fancystore.base.presentation.StringsProvider
import com.ablanco.fancystore.domain.models.Discount
import com.ablanco.fancystore.domain.models.Product
import com.ablanco.fancystore.utils.presentation.formatCurrency
import com.ablanco.fancystore.utils.presentation.iconResId
import com.ablanco.fancystore.utils.presentation.toPresentation

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */

class ProductsPresentationMapper(
    private val stringsProvider: StringsProvider
) {

    fun map(products: List<Product>, discounts: List<Discount>): List<ProductVM> =
        products.map { product ->
            val discount = discounts.find { product.code in it.items }
                ?.toPresentation(stringsProvider)
            product.toPresentation().copy(discount = discount)
        }
}

private fun Product.toPresentation(): ProductVM =
    ProductVM(
        id = code,
        name = name,
        iconResId = iconResId,
        price = price.formatCurrency()
    )
