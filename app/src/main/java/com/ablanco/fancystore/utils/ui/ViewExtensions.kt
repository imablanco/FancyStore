package com.ablanco.fancystore.utils.ui

import android.graphics.Paint
import android.view.View
import android.widget.TextView

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */
fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.switchVisibility(condition: Boolean, turnInvisible: Boolean = false) =
    if (condition) {
        visible()
    } else {
        if (turnInvisible) {
            invisible()
        } else {
            gone()
        }
    }

fun TextView.strikeThrough() {
    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

