package com.ablanco.fancystore.utils.presentation

import java.text.NumberFormat
import java.util.Locale

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */
fun Double.formatDecimal(
    minimumDigits: Int,
    maximumDigits: Int? = null,
    locale: Locale = Locale.getDefault()
): String = runCatching {
    val format = NumberFormat.getInstance(locale).apply {
        minimumFractionDigits = minimumDigits
        maximumDigits?.let { maximumFractionDigits = it }
    }
    format.format(this)
}.getOrNull().orEmpty()
