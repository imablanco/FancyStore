package com.ablanco.fancystore.features.products.presentation

import com.ablanco.fancystore.R
import com.ablanco.fancystore.base.presentation.StringsProvider
import com.ablanco.fancystore.domain.base.CoroutinesDispatchers
import com.ablanco.fancystore.domain.base.Right
import com.ablanco.fancystore.domain.usecases.AddProductToCartUseCase
import com.ablanco.fancystore.domain.usecases.GetCartItemCountUseCase
import com.ablanco.fancystore.domain.usecases.GetDiscountsUseCase
import com.ablanco.fancystore.domain.usecases.GetProductsUseCase
import com.ablanco.fancystore.presentation.*
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
    object CartClicked : ProductsIntent()
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
    private val stringsProvider: StringsProvider,
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

        loadProducts()
    }

    override fun handleIntent(intent: Intent) {
        when (intent) {
            is ProductsIntent.AddProductToCart -> addProductToCart(intent.product.id)
            is ProductsIntent.CartClicked -> dispatchAction(ProductsViewAction.NavigateToCart)
        }
    }

    private fun loadProducts() {
        launch {
            /*As launch operates in Main dispatcher we can safely update UI state here */
            setState { copy(isLoading = true) }
            /*Here we explicitly check products flow error but not discounts. I consider this is a
            * sane default where it products success bot not discounts, users can still continue
            * working but an error in products will make the app unusable*/
            val (productsResponse, discounts) = zipPair(
                /*Thanks to async builders we can execute both coroutines in parallel*/
                async { getProductsUseCase() },
                async { getDiscountsUseCase().rightOrNull().orEmpty() }
            )
            productsResponse.fold(
                right = { products ->
                    val productsVm = presentationMapper.map(products, discounts)
                    setState { copy(isLoading = false, products = productsVm) }
                    if (productsVm.isEmpty()) dispatchAction(ProductsViewAction.ShowEmptyProducts)
                },
                left = {
                    setState { copy(isLoading = false) }
                    dispatchError(
                        /*By using an instance of [RecoverableError] we can provide
                        * a retry on error feature without bloating the UI, just forwarding the
                        * action to retry in the error itself*/
                        RecoverableError(
                            message = stringsProvider(R.string.productsListMessageError),
                            onRetry = ::loadProducts
                        )
                    )
                }
            )
        }
    }

    private fun addProductToCart(productId: String) {
        launch {
            val isSuccess = addProductToCartUseCase(productId) is Right
            if (!isSuccess) {
                dispatchError(
                    DefaultError(message = stringsProvider(R.string.productsAddToCartMessageError))
                )
            }
        }
    }
}
