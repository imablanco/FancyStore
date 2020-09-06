package com.ablanco.fancystore.features.checkout.presentation

import com.ablanco.fancystore.R
import com.ablanco.fancystore.base.presentation.StringsProvider
import com.ablanco.fancystore.domain.base.CoroutinesDispatchers
import com.ablanco.fancystore.domain.usecases.GetCartUseCase
import com.ablanco.fancystore.presentation.*
import kotlinx.coroutines.flow.collect

/**
 * Created by Álvaro Blanco Cabrero on 06/09/2020.
 * FancyStore.
 */

sealed class CheckoutViewAction : ViewAction

sealed class CheckoutIntent : Intent

data class CheckoutViewState(
    val isLoading: Boolean = false,
    val items: List<CheckoutItem> = emptyList()
) : ViewState

class CheckoutViewModel(
    private val getCartUseCase: GetCartUseCase,
    private val stringsProvider: StringsProvider,
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
                        setState { copy(isLoading = false, items = mapper.map(it)) }
                        //TODO emoty state
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

    override fun handleIntent(intent: Intent) {
    }
}
