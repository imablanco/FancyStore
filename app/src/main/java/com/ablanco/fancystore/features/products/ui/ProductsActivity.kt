package com.ablanco.fancystore.features.products.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.ablanco.fancystore.base.ui.BaseActivity
import com.ablanco.fancystore.base.ui.switchVisibility
import com.ablanco.fancystore.databinding.ActivityProductsBinding
import com.ablanco.fancystore.features.products.presentation.ProductsViewAction
import com.ablanco.fancystore.features.products.presentation.ProductsViewModel
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter = ProductsAdapter({})
        binding.rvProducts.layoutManager = GridLayoutManager(this, 2)
        binding.rvProducts.adapter = adapter

        viewModel.viewState.observe(this, Observer { state ->
            binding.viewLoading.switchVisibility(state.isLoading)
            adapter.submitList(state.products)
        })

        viewModel.viewActions.observe(this, Observer { action ->
            when (action) {
                is ProductsViewAction.ShowEmptyProducts ->
                    Snackbar.make(binding.root, "", Snackbar.LENGTH_SHORT)
            }
        })

        if (savedInstanceState == null) {
            viewModel.load()
        }
    }
}
