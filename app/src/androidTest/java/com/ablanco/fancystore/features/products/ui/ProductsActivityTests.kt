@file:Suppress("TestFunctionName")

package com.ablanco.fancystore.features.products.ui

import com.ablanco.fancystore.R
import com.ablanco.fancystore.base.BaseUiTest
import com.ablanco.fancystore.base.MockData
import com.ablanco.fancystore.base.checkSnackBarIsDisplayed
import com.ablanco.fancystore.domain.base.Either
import com.ablanco.fancystore.domain.base.GenericError
import com.ablanco.fancystore.domain.base.Left
import com.ablanco.fancystore.domain.base.Right
import com.ablanco.fancystore.domain.models.Discount
import com.ablanco.fancystore.domain.models.Product
import com.ablanco.fancystore.domain.usecases.AddProductToCartUseCase
import com.ablanco.fancystore.domain.usecases.GetCartItemCountUseCase
import com.ablanco.fancystore.domain.usecases.GetDiscountsUseCase
import com.ablanco.fancystore.domain.usecases.GetProductsUseCase
import com.ablanco.fancystore.features.products.presentation.ProductsViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * Created by Álvaro Blanco Cabrero on 07/09/2020.
 * FancyStore.
 */
class ProductsActivityTests : BaseUiTest() {

    @Test
    fun Given_emptyCart_When_loads_Then_cartBadgeIsGone() {

        /*Given*/
        val emptyCart = Right(0)
        startKoin {
            modules(
                module(override = true) {
                    viewModel {
                        getViewModel(getCartCountResponse = emptyCart)
                    }
                }
            )
        }

        ProductsPageObject().launch().checkCartBadgeIsGone()
    }

    @Test
    fun Given_cartWithItems_When_loads_Then_cartBadgeIsVisible() {

        /*Given*/
        val emptyCart = Right(10)
        startKoin {
            modules(
                module(override = true) {
                    viewModel {
                        getViewModel(getCartCountResponse = emptyCart)
                    }
                }
            )
        }

        ProductsPageObject().launch().checkCartBadgeIsVisible()
    }

    @Test
    fun Given_noProducts_When_loads_Then_showEmptyMessageAndListIsEmpty() {
        /*Given*/
        val emptyProducts: Either<List<Product>> = Right(emptyList())
        startKoin {
            modules(
                module(override = true) {
                    viewModel {
                        getViewModel(productsResponse = emptyProducts)
                    }
                }
            )
        }

        ProductsPageObject().launch()
            .checkSnackBarIsDisplayed(R.string.productsListMessageEmpty)
            .checkListIsEmpty()
    }

    @Test
    fun Given_products_When_loadsSuccessfully_Then_listHasProducts() {
        /*Given*/
        val products: Either<List<Product>> = Right(MockData.mockProducts)
        startKoin {
            modules(
                module(override = true) {
                    viewModel {
                        getViewModel(productsResponse = products)
                    }
                }
            )
        }

        ProductsPageObject().launch().checkListIsNotEmpty()
    }

    @Test
    fun Given_addToCartClicked_When_fails_Then_errorIsShown() {
        /*Given*/
        startKoin {
            modules(
                module(override = true) {
                    viewModel {
                        getViewModel(addProductToCartResponse = Left(GenericError))
                    }
                }
            )
        }

        ProductsPageObject().launch()
            .clickAddToProduct()
            .checkSnackBarIsDisplayed(R.string.productsAddToCartMessageError)
    }

    @Test
    fun When_clicked_cart_action_Then_navigates_to_checkout() {

        /*Given*/
        startKoin { modules(module { viewModel { getViewModel() } }) }

        ProductsPageObject().launch()
            .clickCart()
            .checkNavigatedToCheckout()
    }

    private fun getViewModel(
        productsResponse: Either<List<Product>> = Right(MockData.mockProducts),
        discountsResponse: Either<List<Discount>> = Right(MockData.mockDiscounts),
        getCartCountResponse: Either<Int> = Right(0),
        addProductToCartResponse: Either<Unit> = Right(Unit)
    ): ProductsViewModel {

        return ProductsViewModel(
            getProductsUseCase = object : GetProductsUseCase {
                override suspend fun invoke(): Either<List<Product>> = productsResponse
            },
            getDiscountsUseCase = object : GetDiscountsUseCase {
                override suspend fun invoke(): Either<List<Discount>> = discountsResponse
            },
            getCartItemCountUseCase = object : GetCartItemCountUseCase {
                override suspend fun invoke(): Flow<Either<Int>> = flowOf(getCartCountResponse)
            },
            addProductToCartUseCase = object : AddProductToCartUseCase {
                override suspend fun invoke(productId: String): Either<Unit> =
                    addProductToCartResponse
            },
            stringsProvider = stringsProvider,
            dispatchers = dispatchers
        )
    }
}
