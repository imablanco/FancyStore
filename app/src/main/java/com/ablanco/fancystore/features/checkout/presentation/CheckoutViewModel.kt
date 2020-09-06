package com.ablanco.fancystore.features.checkout.presentation

import com.ablanco.fancystore.R
import com.ablanco.fancystore.base.presentation.StringsProvider
import com.ablanco.fancystore.domain.base.CoroutinesDispatchers
import com.ablanco.fancystore.domain.base.Right
import com.ablanco.fancystore.domain.usecases.GetCartUseCase
import com.ablanco.fancystore.domain.usecases.PayCartUseCase
import com.ablanco.fancystore.presentation.*
import kotlinx.coroutines.flow.collect

/**
 * Created by Álvaro Blanco Cabrero on 06/09/2020.
 * FancyStore.
 */

sealed class CheckoutViewAction : ViewAction {
    object ShowPaymentConfirmation : CheckoutViewAction()
}

sealed class CheckoutIntent : Intent {

    object ContinueWithPayment : CheckoutIntent()
}

data class CheckoutViewState(
    val isLoading: Boolean = false,
    val items: List<CheckoutItem> = emptyList(),
    val isPayEnabled: Boolean = false
) : ViewState

class CheckoutViewModel(
    private val getCartUseCase: GetCartUseCase,
    private val stringsProvider: StringsProvider,
    private val payCartUseCase: PayCartUseCase,
    dispatchers: CoroutinesDispatchers
) : BaseViewModel<CheckoutViewState, CheckoutViewAction, CheckoutIntent>(dispatchers) {

    override val initialViewState: CheckoutViewState = CheckoutViewState()

    private val mapper = CheckoutPresentationMapper(stringsProvider)

    override fun load() {
        launch {
            setState { copy(isLoading = true) }
            getCartUseCase().collect {
                it.fold(
                    {
                        setState {
                            copy(
                                isLoading = false,
                                items = mapper.map(it),
                                isPayEnabled = it.products.isNotEmpty()
                            )
                        }
                    },
                    {
                        setState { copy(isLoading = false) }
                        dispatchError(
                            DefaultError(message = stringsProvider(R.string.checkoutError))
                        )
                    }
                )
            }
        }
    }

    override fun handleIntent(intent: CheckoutIntent) = when (intent) {
        is CheckoutIntent.ContinueWithPayment -> payCart()
    }

    private fun payCart() {
        launch {
            val isSuccess = payCartUseCase() is Right
            if (isSuccess) dispatchAction(CheckoutViewAction.ShowPaymentConfirmation)
            else dispatchError(DefaultError(stringsProvider(R.string.checkoutPaymentError)))
        }
    }
}
