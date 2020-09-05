package com.ablanco.fancystore.features.products.presentation

import androidx.annotation.DrawableRes
import com.ablanco.fancystore.R
import com.ablanco.fancystore.base.presentation.StringsProvider
import com.ablanco.fancystore.utils.presentation.formatCurrency
import com.ablanco.fancystore.utils.presentation.formatDecimal
import com.ablanco.fancystore.domain.models.BulkDiscount
import com.ablanco.fancystore.domain.models.Discount
import com.ablanco.fancystore.domain.models.FreeItemDiscount
import com.ablanco.fancystore.domain.models.Product

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */

/*Product url should indeed come from back and having hc ids here will never be an option. But it is
* just for presentation purposes*/
private const val IdVoucher = "VOUCHER"
private const val IdShirt = "TSHIRT"
private const val IdMug = "MUG"

class ProductsPresentationMapper(
    private val stringsProvider: StringsProvider
) {

    fun map(products: List<Product>, discounts: List<Discount>): List<ProductVM> =
        products.map { product ->
            val discount = discounts
                .find { product.code in it.items }
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

private fun Discount.toPresentation(stringsProvider: StringsProvider): DiscountVM = when (this) {
    is FreeItemDiscount -> DiscountVM(
        displayName = "$minAmount x $freeAmount",
        description = stringsProvider(
            R.string.discountFreeDesc,
            minAmount.toString(),
            freeAmount.toString()
        )
    )
    is BulkDiscount -> {
        val discountPercentage = "${(priceFactor * 100).formatDecimal(minimumDigits = 0)}%"
        DiscountVM(
            displayName = stringsProvider(
                R.string.discountBulkShortDesc,
                discountPercentage,
                minAmount.toString()
            ),
            description = stringsProvider(
                R.string.discountBulkDesc,
                minAmount.toString(),
                discountPercentage
            )
        )
    }
}

@get:DrawableRes
private val Product.iconResId: Int
    get() = when (code) {
        IdVoucher -> R.drawable.ic_voucher
        IdShirt -> R.drawable.ic_shirt
        IdMug -> R.drawable.ic_mug
        else -> 0
    }
