package com.ablanco.fancystore.utils.ui

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding

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

/**
 * Extension to connect this [Activity] with its [ViewBinding]
 */
fun <T : ViewBinding> Activity.viewBinding(inflate: (LayoutInflater) -> T) = lazy {
    inflate(layoutInflater)
}
