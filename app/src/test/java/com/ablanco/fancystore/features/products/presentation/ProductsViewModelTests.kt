package com.ablanco.fancystore.features.products.presentation

import com.ablanco.fancystore.base.BaseViewModelTest
import com.ablanco.fancystore.base.TestObserver
import com.ablanco.fancystore.domain.base.Either
import com.ablanco.fancystore.domain.base.GenericError
import com.ablanco.fancystore.domain.base.Left
import com.ablanco.fancystore.domain.base.Right
import com.ablanco.fancystore.domain.models.Discount
import com.ablanco.fancystore.domain.models.Product
import com.ablanco.fancystore.mocks.MockData
import com.ablanco.fancystore.presentation.PresentationError
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert
import org.junit.Test

/**
 * Created by Álvaro Blanco Cabrero on 06/09/2020.
 * FancyStore.
 */
class ProductsViewModelTests : BaseViewModelTest() {

    @Test
    fun `Given empty cart When VM loads Then cartItemCount is 0 and showCartItemCount is false`() {
        /*Given*/
        val emptyCart = Right(0)
        val viewModel = getViewModel(getCartCountResponse = emptyCart)
        /*Normally one would use Mockito verifications with ArgumentCaptors to assert on
        * values but one of the benefits of this MVI kind-of arch is that all the state is driven
        * trough the ViewState class and just by checking this class as if it was a snapshot
        * at any given moment*/
        val testObserver = TestObserver<ProductsViewState>()
        viewModel.viewState.observeForever(testObserver)

        /*When*/
        viewModel.load()

        /*Then*/
        Assert.assertFalse(testObserver.lastValue.showCartItemCount)
        Assert.assertEquals(testObserver.lastValue.cartItemCount, "0")
    }

    @Test
    fun `Given no products When VM loads Then ShowEmptyProducts is dispatched`() {
        /*Given*/
        val viewModel = getViewModel(productsResponse = Right(emptyList()))
        val testObserver = TestObserver<ProductsViewAction>()
        viewModel.viewActions.observeForever(testObserver)

        /*When*/
        viewModel.load()

        /*Then*/
        Assert.assertTrue(testObserver.lastValue is ProductsViewAction.ShowEmptyProducts)
    }

    @Test
    fun `Given a list of products When VM loads and is success Then products has items`() {
        /*Given*/
        val products = Right(MockData.mockProducts)
        val viewModel = getViewModel(productsResponse = products)
        val testObserver = TestObserver<ProductsViewState>()
        viewModel.viewState.observeForever(testObserver)

        /*When*/
        viewModel.load()

        /*Then*/
        Assert.assertTrue(testObserver.lastValue.products.isNotEmpty())
    }

    @Test
    fun `Given discounts When VM loads Then products has discounts`() {
        /*Given*/
        val products = Right(MockData.mockProducts)
        val discounts = Right(MockData.mockDiscounts)
        val viewModel = getViewModel(
            productsResponse = products,
            discountsResponse = discounts
        )
        val testObserver = TestObserver<ProductsViewState>()
        viewModel.viewState.observeForever(testObserver)

        /*When*/
        viewModel.load()

        /*Then*/
        Assert.assertTrue(testObserver.lastValue.products.any { it.hasDiscount })
    }

    @Test
    fun `Given no discounts When VM loads Then products has no discounts`() {
        /*Given*/
        val products = Right(MockData.mockProducts)
        val discounts = Right<List<Discount>>(emptyList())
        val viewModel = getViewModel(
            productsResponse = products,
            discountsResponse = discounts
        )
        val testObserver = TestObserver<ProductsViewState>()
        viewModel.viewState.observeForever(testObserver)

        /*When*/
        viewModel.load()

        /*Then*/
        Assert.assertTrue(testObserver.lastValue.products.none { it.hasDiscount })
    }

    @Test
    fun `Given a list of products When VM loads and is error Then products has no items`() {
        /*Given*/
        val viewModel = getViewModel(productsResponse = Left(GenericError))
        val testObserver = TestObserver<ProductsViewState>()
        viewModel.viewState.observeForever(testObserver)

        /*When*/
        viewModel.load()

        /*Then*/
        Assert.assertTrue(testObserver.lastValue.products.isEmpty())
    }

    @Test
    fun `Given a list of products When VM loads and is error Then error is dispatched`() {
        /*Given*/
        val viewModel = getViewModel(productsResponse = Left(GenericError))
        val testObserver = TestObserver<PresentationError>()
        viewModel.viewErrors.observeForever(testObserver)

        /*When*/
        viewModel.load()

        /*Then*/
        Assert.assertNotNull(testObserver.lastValue)
    }

    @Test
    fun `Given a list of products When user adds one and is error Then error is dispatched`() {
        /*Given*/
        val viewModel = getViewModel(addProductToCartResponse = Left(GenericError))
        val testObserver = TestObserver<PresentationError>()
        viewModel.viewErrors.observeForever(testObserver)

        /*When*/
        viewModel.load()
        val subject = viewModel.getState().products.first()
        viewModel.sendIntent(ProductsIntent.AddProductToCart(subject))

        /*Then*/
        Assert.assertNotNull(testObserver.lastValue)
    }

    @Test
    fun `When user clicks cart icon Then NavigateToCart is dispatched`() {
        /*Given*/
        val viewModel = getViewModel()
        val testObserver = TestObserver<ProductsViewAction>()
        viewModel.viewActions.observeForever(testObserver)

        /*When*/
        viewModel.load()
        viewModel.sendIntent(ProductsIntent.CartClicked)

        /*Then*/
        Assert.assertTrue(testObserver.lastValue is ProductsViewAction.NavigateToCart)
    }

    private fun getViewModel(
        productsResponse: Either<List<Product>> = Right(MockData.mockProducts),
        discountsResponse: Either<List<Discount>> = Right(MockData.mockDiscounts),
        getCartCountResponse: Either<Int> = Right(0),
        addProductToCartResponse: Either<Unit> = Right(Unit)
    ): ProductsViewModel {

        return ProductsViewModel(
            getProductsUseCase = mock {
                onBlocking { invoke() } doReturn productsResponse
            },
            getDiscountsUseCase = mock {
                onBlocking { invoke() } doReturn discountsResponse
            },
            getCartItemCountUseCase = mock {
                onBlocking { invoke() } doReturn flowOf(getCartCountResponse)
            },
            addProductToCartUseCase = mock {
                onBlocking { invoke(any()) } doReturn addProductToCartResponse
            },
            stringsProvider = stringsProvider,
            dispatchers = dispatchers
        )
    }
}
