package com.ablanco.fancystore.features.products.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ablanco.fancystore.databinding.ItemProductBinding
import com.ablanco.fancystore.features.products.presentation.ProductVM
import com.ablanco.fancystore.features.products.presentation.hasDiscount
import com.ablanco.fancystore.utils.ui.*

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

            binding.layoutDiscount.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    //noinspection WrongConstant
                    binding.tvDiscountDesc.showReveal(Top or Right)
                }
            }

            binding.tvDiscountDesc.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    //noinspection WrongConstant
                    binding.tvDiscountDesc.hideReveal(Top or Right)
                }
            }
        }

        fun bind(product: ProductVM) {
            with(binding) {
                ivProductIcon.setImageResource(product.iconResId)
                tvProductName.text = product.name
                tvProductPrice.text = product.price
                layoutDiscount.switchVisibility(product.hasDiscount)
                tvDiscountShortDesc.text = product.discount?.displayName
                tvDiscountDesc.invisible()
                tvDiscountDesc.text = product.discount?.description
            }
        }
    }
}
