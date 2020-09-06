package com.ablanco.fancystore.data.models

import com.ablanco.fancystore.domain.models.Discount
import com.ablanco.fancystore.domain.models.ItemsPromoDiscount
import com.ablanco.fancystore.domain.models.Product

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
