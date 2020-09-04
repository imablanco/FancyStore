package com.ablanco.fancystore.features.products.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ablanco.fancystore.base.ui.DefaultItemCallback
import com.ablanco.fancystore.base.ui.ViewBindingViewHolder
import com.ablanco.fancystore.databinding.ItemProductBinding
import com.ablanco.fancystore.features.products.presentation.ProductVM

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */
class ProductsAdapter(
    private val onAddProductToBag: (ProductVM) -> Unit
) : ListAdapter<ProductVM, ProductsAdapter.ProductViewHolder>(
    DefaultItemCallback(ProductVM::id)
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder =
        ProductViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class ProductViewHolder(binding: ItemProductBinding) :
        ViewBindingViewHolder<ItemProductBinding>(binding) {

        init {
            binding.ivAddToBag.setOnClickListener {
                /*Very important to check this to avoid getting invalid position while RVs
                * is doing a layout pass*/
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onAddProductToBag(getItem(adapterPosition))
                }
            }
        }

        fun bind(product: ProductVM) {
            with(binding) {
                ivProductIcon.setImageResource(product.iconResId)
                tvProductName.text = product.name
                tvProductPrice.text = product.price
            }
        }
    }
}
