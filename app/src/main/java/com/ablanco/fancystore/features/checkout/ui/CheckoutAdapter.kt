@file:Suppress("FunctionName")

package com.ablanco.fancystore.features.checkout.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ablanco.fancystore.databinding.ItemCheckoutProductBinding
import com.ablanco.fancystore.databinding.ItemCheckoutResumeBinding
import com.ablanco.fancystore.features.checkout.presentation.CheckoutItem
import com.ablanco.fancystore.features.checkout.presentation.CheckoutProduct
import com.ablanco.fancystore.features.checkout.presentation.CheckoutResume
import com.ablanco.fancystore.utils.ui.DefaultItemCallback
import com.ablanco.fancystore.utils.ui.ViewBindingViewHolder
import com.ablanco.fancystore.utils.ui.strikeThrough
import com.ablanco.fancystore.utils.ui.switchVisibility

/**
 * Created by Álvaro Blanco Cabrero on 05/09/2020.
 * FancyStore.
 */

private const val ItemTypeProduct = 0
private const val ItemTypeResume = 1

/*Normally I use Epoxy in situations like this where there are different view types involved,
* but in order to avoid bloating the code with too many libs I ended up doing it with vanilla
* RV adapter*/
class CheckoutAdapter :
    ListAdapter<CheckoutItem, RecyclerView.ViewHolder>(DefaultItemCallback()) { //TODO explain why we cannot rely on product id to perform diffing

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            ItemTypeProduct -> ProductViewHolder(
                ItemCheckoutProductBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            ItemTypeResume -> ResumeViewHolder(
                ItemCheckoutResumeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalStateException("Unknown state") /* Fail fast if new view types are added and not handled*/
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is CheckoutProduct -> ItemTypeProduct
        is CheckoutResume -> ItemTypeResume
    }

    private class ProductViewHolder(binding: ItemCheckoutProductBinding) :
        ViewBindingViewHolder<ItemCheckoutProductBinding>(binding) {

        init {
            binding.tvProductOriginalPrice.strikeThrough()
        }

        fun bind(product: CheckoutProduct) {
            with(binding) {
                ivProductIcon.setImageResource(product.iconResId)
                tvProductName.text = product.name
                tvProductOriginalPrice.text = product.originalPrice
                tvProductPrice.text = product.finalPrice
                tvProductOriginalPrice.switchVisibility(product.hasDiscount)
            }
        }
    }

    private class ResumeViewHolder(binding: ItemCheckoutResumeBinding) :
        ViewBindingViewHolder<ItemCheckoutResumeBinding>(binding) {

        fun bind(resume: CheckoutResume) {
            with(binding) {
                tvTotal.text = resume.total
                tvSubtotal.text = resume.subtotal
                tvAppliedPromosDiscount.text = resume.discount
                tvAppliedPromos.text = resume.discountInfo
                groupDiscount.switchVisibility(resume.discount != null)
            }
        }
    }
}
