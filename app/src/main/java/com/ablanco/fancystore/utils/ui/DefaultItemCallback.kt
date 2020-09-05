@file:Suppress("FunctionName")

package com.ablanco.fancystore.utils.ui

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 *
 * Default implementation of [DiffUtil.ItemCallback] that checks for item equality by checking
 * [idSelector]. If no [idSelector] is provided, the class will simply compare one item to another
 */
class DefaultItemCallback<T>(private val idSelector: ((T) -> Any?)? = null) :
    DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return idSelector?.let { return it(oldItem) == it(newItem) } ?: oldItem == newItem
    }

    /**
     * Note that equal checking on data classes compares field by field but in other classes
     * typically you'll implement [Any.equals] and [Any.hashCode], and use it to compare object
     * contents.
     */
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
}
