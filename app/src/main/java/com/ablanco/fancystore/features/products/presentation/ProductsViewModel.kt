package com.ablanco.fancystore.features.products.presentation

import com.ablanco.fancystore.domain.base.CoroutinesDispatchers
import com.ablanco.fancystore.domain.models.Product
import com.ablanco.fancystore.domain.usecases.GetProductsUseCase
import com.ablanco.fancystore.presentation.BaseViewModel
import com.ablanco.fancystore.presentation.Intent
import com.ablanco.fancystore.presentation.ViewAction
import com.ablanco.fancystore.presentation.ViewState

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */

sealed class ProductsViewAction : ViewAction {
    object ShowEmptyProducts : ProductsViewAction()
}

sealed class ProductsIntent : Intent

data class ProductsViewState(
    val isLoading: Boolean = false,
    val products: List<ProductVM> = emptyList()
) : ViewState

class ProductsViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    dispatchers: CoroutinesDispatchers
) : BaseViewModel<ProductsViewState, ProductsViewAction, ProductsIntent>(dispatchers) {

    override val initialViewState: ProductsViewState = ProductsViewState()

    override fun load() {
        launch {
            /*As launch operates in Main dispatcher we can safely update UI state here */
            setState { copy(isLoading = true) }
            val products = getProductsUseCase().rightOrNull().orEmpty()
            setState { copy(isLoading = false, products = products.map(Product::toPresentation)) }
            if (products.isEmpty()) dispatchAction(ProductsViewAction.ShowEmptyProducts)
        }
    }

    override fun handleIntent(intent: Intent) {
    }
}
