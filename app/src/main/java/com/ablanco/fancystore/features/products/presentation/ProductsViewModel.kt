package com.ablanco.fancystore.features.products.presentation

import com.ablanco.fancystore.base.presentation.StringsProvider
import com.ablanco.fancystore.domain.base.CoroutinesDispatchers
import com.ablanco.fancystore.domain.base.Right
import com.ablanco.fancystore.domain.usecases.AddProductToCartUseCase
import com.ablanco.fancystore.domain.usecases.GetCartItemCountUseCase
import com.ablanco.fancystore.domain.usecases.GetDiscountsUseCase
import com.ablanco.fancystore.domain.usecases.GetProductsUseCase
import com.ablanco.fancystore.presentation.BaseViewModel
import com.ablanco.fancystore.presentation.Intent
import com.ablanco.fancystore.presentation.ViewAction
import com.ablanco.fancystore.presentation.ViewState
import com.ablanco.fancystore.utils.presentation.zipPair
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */

sealed class ProductsViewAction : ViewAction {
    object ShowEmptyProducts : ProductsViewAction()

    /*Maybe for big apps its better to have routing actions in a separated logical class but
    * as it is purely a ViewAction, I normally prefer to keep navigation actions here for the sake
    * of simplicity*/
    object NavigateToCart : ProductsViewAction()
}

sealed class ProductsIntent : Intent {

    class AddProductToCart(val product: ProductVM) : ProductsIntent()

    /*Some Intents, as navigation actions that do not require arguments, can be resolved directly
    * in the View, but I prefer to drive all user interactions through the VM to respect the
    * data flow. Also maybe in a future this action may involve some kind of business logic (i.e
    * sending some data to analytics) so it would require to move it here*/
    object NavigateToCart : ProductsIntent()
}

data class ProductsViewState(
    val isLoading: Boolean = false,
    val products: List<ProductVM> = emptyList(),
    val showCartItemCount: Boolean = false,
    val cartItemCount: String? = null
) : ViewState

class ProductsViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val getDiscountsUseCase: GetDiscountsUseCase,
    private val addProductToCartUseCase: AddProductToCartUseCase,
    private val getCartItemCountUseCase: GetCartItemCountUseCase,
    stringsProvider: StringsProvider,
    dispatchers: CoroutinesDispatchers
) : BaseViewModel<ProductsViewState, ProductsViewAction, ProductsIntent>(dispatchers) {

    override val initialViewState: ProductsViewState = ProductsViewState()

    private val presentationMapper = ProductsPresentationMapper(stringsProvider)

    override fun load() {

        launch {
            getCartItemCountUseCase().collect {
                val itemCount = it.rightOrNull() ?: 0
                setState {
                    copy(
                        cartItemCount = itemCount.toString(),
                        showCartItemCount = itemCount != 0
                    )
                }
            }
        }
        launch {
            /*As launch operates in Main dispatcher we can safely update UI state here */
            setState { copy(isLoading = true) }
            /*Thanks to async builders we can execute both coroutines in parallel*/
            val (products, discounts) = zipPair(
                async { getProductsUseCase().rightOrNull().orEmpty() },
                async { getDiscountsUseCase().rightOrNull().orEmpty() }
            )
            setState {
                copy(
                    isLoading = false,
                    products = presentationMapper.map(products, discounts)
                )
            }
            if (products.isEmpty()) dispatchAction(ProductsViewAction.ShowEmptyProducts)
        }
    }

    override fun handleIntent(intent: Intent) {
        when (intent) {
            is ProductsIntent.AddProductToCart -> addProductToCart(intent.product.id)
            is ProductsIntent.NavigateToCart -> dispatchAction(ProductsViewAction.NavigateToCart)
        }
    }

    private fun addProductToCart(productId: String) {
        launch {
            val isSuccess = addProductToCartUseCase(productId) is Right
            //TODO show error
        }
    }
}
