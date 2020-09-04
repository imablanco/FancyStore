package com.ablanco.fancystore.data.models

import com.ablanco.fancystore.domain.models.BulkDiscount
import com.ablanco.fancystore.domain.models.Discount
import com.ablanco.fancystore.domain.models.FreeItemDiscount
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
    is FreeItemDiscountData -> FreeItemDiscount(
        items = items,
        minAmount = minAmount,
        freeAmount = freeAmount
    )
    is BulkDiscountData -> BulkDiscount(
        items = items,
        minAmount = minAmount,
        priceFactor = priceFactor
    )
}
