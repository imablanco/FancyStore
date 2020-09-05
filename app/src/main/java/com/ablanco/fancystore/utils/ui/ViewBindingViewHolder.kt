package com.ablanco.fancystore.utils.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */
abstract class ViewBindingViewHolder<V : ViewBinding>(val binding: V) :
    RecyclerView.ViewHolder(binding.root)
