package com.ablanco.fancystore.utils.ui

import android.app.Activity
import android.content.res.Configuration
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

/**
 * Created by Álvaro Blanco Cabrero on 05/09/2020.
 * FancyStore.
 */

/**
 * Extension to connect this [Activity] with its [ViewBinding]
 */
fun <T : ViewBinding> Activity.viewBinding(inflate: (LayoutInflater) -> T) = lazy {
    inflate(layoutInflater)
}

val Activity.isInPortrait: Boolean
    get() = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
