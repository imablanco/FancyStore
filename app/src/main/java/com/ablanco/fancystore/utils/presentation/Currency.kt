package com.ablanco.fancystore.utils.presentation

import java.text.NumberFormat
import java.util.Locale

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */

fun Double.formatCurrency(locale: Locale = Locale.getDefault()): String {
    val formatter = NumberFormat.getCurrencyInstance(locale)
    return formatter.format(this)
}
