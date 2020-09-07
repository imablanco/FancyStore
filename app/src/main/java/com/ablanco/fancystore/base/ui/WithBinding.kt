package com.ablanco.fancystore.base.ui

import androidx.viewbinding.ViewBinding

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */
interface WithBinding<V : ViewBinding> {

    val binding: V
}
