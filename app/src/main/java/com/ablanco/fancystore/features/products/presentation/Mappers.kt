package com.ablanco.fancystore.features.products.presentation

import androidx.annotation.DrawableRes
import com.ablanco.fancystore.R
import com.ablanco.fancystore.base.presentation.formatCurrency
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

fun Product.toPresentation(): ProductVM =
    ProductVM(
        id = code,
        name = name,
        iconResId = iconResId,
        price = price.formatCurrency()
    )

@get:DrawableRes
private val Product.iconResId: Int
    get() = when (code) {
        IdVoucher -> R.drawable.ic_voucher
        IdShirt -> R.drawable.ic_shirt
        IdMug -> R.drawable.ic_mug
        else -> 0
    }
