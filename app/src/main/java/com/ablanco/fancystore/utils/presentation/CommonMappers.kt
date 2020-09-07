package com.ablanco.fancystore.utils.presentation

import androidx.annotation.DrawableRes
import com.ablanco.fancystore.R
import com.ablanco.fancystore.base.presentation.StringsProvider
import com.ablanco.fancystore.domain.models.*

/**
 * Created by Álvaro Blanco Cabrero on 06/09/2020.
 * FancyStore.
 */
/*Product url should indeed come from back and having hc ids here will never be an option. But it is
* just for presentation purposes*/
private const val IdVoucher = "VOUCHER"
private const val IdShirt = "TSHIRT"
private const val IdMug = "MUG"

@get:DrawableRes
val Product.iconResId: Int
    get() = code.iconResId

@get:DrawableRes
val CartProduct.iconResId: Int
    get() = code.iconResId

@get:DrawableRes
private val String.iconResId: Int
    get() = when (this) {
        IdVoucher -> R.drawable.ic_voucher
        IdShirt -> R.drawable.ic_shirt
        IdMug -> R.drawable.ic_mug
        else -> 0
    }

fun Discount.toPresentation(stringsProvider: StringsProvider): DiscountVM = when (this) {
    is FreeItemDiscount -> DiscountVM(
        displayName = "$minAmount x ${minAmount - freeAmount}",
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
