package com.ablanco.fancystore.features.products.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.ablanco.fancystore.R
import com.ablanco.fancystore.base.ui.BaseActivity
import com.ablanco.fancystore.base.ui.ErrorDisplayObserver
import com.ablanco.fancystore.databinding.ActivityProductsBinding
import com.ablanco.fancystore.databinding.ViewCartMenuIconBinding
import com.ablanco.fancystore.features.checkout.ui.CheckoutActivity
import com.ablanco.fancystore.features.products.presentation.ProductsIntent
import com.ablanco.fancystore.features.products.presentation.ProductsViewAction
import com.ablanco.fancystore.features.products.presentation.ProductsViewModel
import com.ablanco.fancystore.utils.ui.isInPortrait
import com.ablanco.fancystore.utils.ui.switchVisibility
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */
class ProductsActivity : BaseActivity<ActivityProductsBinding>() {

    override val getBinding: (LayoutInflater) -> ActivityProductsBinding =
        ActivityProductsBinding::inflate

    private val viewModel: ProductsViewModel by viewModel()

    private val cartMenuBinding: ViewCartMenuIconBinding?
        get() = binding.toolbar.menu.findItem(R.id.actionCart)?.actionView?.let(
            ViewCartMenuIconBinding::bind
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter = ProductsAdapter {
            viewModel.sendIntent(ProductsIntent.AddProductToCart(it))
        }
        val gridColumns = if (isInPortrait) 2 else 3
        binding.rvProducts.layoutManager = GridLayoutManager(this, gridColumns)
        binding.rvProducts.adapter = adapter
        cartMenuBinding?.root?.setOnClickListener {
            viewModel.sendIntent(ProductsIntent.CartClicked)
        }

        viewModel.viewState.observe(this, Observer { state ->
            binding.viewLoading.switchVisibility(state.isLoading)
            adapter.submitList(state.products)
            cartMenuBinding?.tvCartItemCount?.switchVisibility(state.showCartItemCount)
            cartMenuBinding?.tvCartItemCount?.text = state.cartItemCount.toString()

        })

        viewModel.viewActions.observe(this, Observer { action ->
            when (action) {
                is ProductsViewAction.ShowEmptyProducts ->
                    Snackbar.make(
                        binding.root,
                        R.string.productsListMessageEmpty,
                        Snackbar.LENGTH_SHORT
                    ).show()
                is ProductsViewAction.NavigateToCart ->
                    startActivity(Intent(this, CheckoutActivity::class.java))
            }
        })

        viewModel.viewErrors.observe(this, ErrorDisplayObserver())

        if (savedInstanceState == null) {
            viewModel.load()
        }
    }
}
