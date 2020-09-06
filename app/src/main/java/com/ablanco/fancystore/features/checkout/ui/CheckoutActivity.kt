package com.ablanco.fancystore.features.checkout.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ablanco.fancystore.R
import com.ablanco.fancystore.base.ui.BaseActivity
import com.ablanco.fancystore.base.ui.ErrorDisplayObserver
import com.ablanco.fancystore.databinding.ActivityCheckoutBinding
import com.ablanco.fancystore.features.checkout.presentation.CheckoutIntent
import com.ablanco.fancystore.features.checkout.presentation.CheckoutViewAction
import com.ablanco.fancystore.features.checkout.presentation.CheckoutViewModel
import com.ablanco.fancystore.utils.ui.switchVisibility
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Álvaro Blanco Cabrero on 05/09/2020.
 * FancyStore.
 */
class CheckoutActivity : BaseActivity<ActivityCheckoutBinding>() {

    override val getBinding: (LayoutInflater) -> ActivityCheckoutBinding =
        ActivityCheckoutBinding::inflate

    private val viewModel: CheckoutViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter = CheckoutAdapter()
        binding.rvCartProducts.layoutManager = LinearLayoutManager(this)
        binding.rvCartProducts.adapter = adapter
        binding.btPay.setOnClickListener {
            viewModel.sendIntent(CheckoutIntent.ContinueWithPayment)
        }

        viewModel.viewState.observe(this, Observer { state ->
            binding.viewLoading.switchVisibility(state.isLoading)
            binding.tvEmptyState.switchVisibility(state.items.isEmpty())
            binding.btPay.isEnabled = state.isPayEnabled
            adapter.submitList(state.items)
        })

        viewModel.viewActions.observe(this, Observer { action ->
            when (action) {
                is CheckoutViewAction.ShowPaymentConfirmation ->
                    Snackbar.make(
                        binding.root,
                        R.string.checkoutPaymentSuccess,
                        Snackbar.LENGTH_SHORT
                    ).show()
            }
        })

        viewModel.viewErrors.observe(this, ErrorDisplayObserver())

        if (savedInstanceState == null) {
            viewModel.load()
        }
    }
}
