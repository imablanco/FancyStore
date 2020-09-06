package com.ablanco.fancystore.utils.presentation

import androidx.annotation.DrawableRes
import com.ablanco.fancystore.R
import com.ablanco.fancystore.base.presentation.StringsProvider
import com.ablanco.fancystore.domain.models.CartProduct
import com.ablanco.fancystore.domain.models.Discount
import com.ablanco.fancystore.domain.models.ItemsPromoDiscount
import com.ablanco.fancystore.domain.models.Product
import kotlin.math.floor

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
    is ItemsPromoDiscount -> {
        /*Although from a data model perspective MxN and x% off buying N+ are the same,
        * conceptually they are different so we have to figure out what logical type we have*/
        val isFreeItemDiscount = priceFactor == 1.0
        val freeAmount = floor(minAmount * amountFactor).toInt()
        val discountPercentage = "${(priceFactor * 100).formatDecimal(minimumDigits = 0)}%"
        val displayName =
            if (isFreeItemDiscount) "$minAmount x $freeAmount"
            else stringsProvider(
                R.string.discountBulkShortDesc,
                discountPercentage,
                minAmount.toString()
            )
        val description =
            if (isFreeItemDiscount) stringsProvider(
                R.string.discountFreeDesc,
                minAmount.toString(),
                freeAmount.toString()
            )
            else stringsProvider(
                R.string.discountBulkDesc,
                minAmount.toString(),
                discountPercentage
            )
        DiscountVM(
            displayName = displayName,
            description = description
        )
    }
}
