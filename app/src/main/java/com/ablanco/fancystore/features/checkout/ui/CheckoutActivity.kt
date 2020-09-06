package com.ablanco.fancystore.features.checkout.ui

import android.view.LayoutInflater
import com.ablanco.fancystore.base.ui.BaseActivity
import com.ablanco.fancystore.databinding.ActivityCheckoutBinding

/**
 * Created by Álvaro Blanco Cabrero on 05/09/2020.
 * FancyStore.
 */
class CheckoutActivity : BaseActivity<ActivityCheckoutBinding>() {

    override val getBinding: (LayoutInflater) -> ActivityCheckoutBinding =
        ActivityCheckoutBinding::inflate
}
