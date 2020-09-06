package com.ablanco.fancystore.features.checkout.presentation

import com.ablanco.fancystore.base.BaseViewModelTest
import com.ablanco.fancystore.base.TestObserver
import com.ablanco.fancystore.domain.base.Either
import com.ablanco.fancystore.domain.base.GenericError
import com.ablanco.fancystore.domain.base.Left
import com.ablanco.fancystore.domain.base.Right
import com.ablanco.fancystore.domain.models.Cart
import com.ablanco.fancystore.domain.models.Product
import com.ablanco.fancystore.mocks.MockData
import com.ablanco.fancystore.presentation.PresentationError
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert
import org.junit.Test

/**
 * Created by Álvaro Blanco Cabrero on 06/09/2020.
 * FancyStore.
 */

private val cartProductsWithDiscount = listOf(
    Product(
        code = "VOUCHER",
        name = "Cabify Voucher",
        price = 5.0
    ), Product(
        code = "VOUCHER",
        name = "Cabify Voucher",
        price = 5.0
    ),
    Product(
        code = "TSHIRT",
        name = "Cabify T-Shirt",
        price = 20.0
    ),
    Product(
        code = "TSHIRT",
        name = "Cabify T-Shirt",
        price = 20.0
    ),
    Product(
        code = "TSHIRT",
        name = "Cabify T-Shirt",
        price = 20.0
    ),
    Product(
        code = "MUG",
        name = "Cabify Coffee Mug",
        price = 7.5
    )
)

class CheckoutViewModelTests : BaseViewModelTest() {

    @Test
    fun `Given empty cart When VM loads Then products is empty`() {
        /*Given*/
        val emptyCart = Right(MockData.buildMockCart(emptyList()))
        val viewModel = getViewModel(cartResponse = emptyCart)
        val testObserver = TestObserver<CheckoutViewState>()
        viewModel.viewState.observeForever(testObserver)

        /*When*/
        viewModel.load()

        /*Then*/
        Assert.assertTrue(testObserver.lastValue.items.isEmpty())
    }

    @Test
    fun `Given empty cart When VM loads Then pay is disabled`() {
        /*Given*/
        val emptyCart = Right(MockData.buildMockCart(emptyList()))
        val viewModel = getViewModel(cartResponse = emptyCart)
        val testObserver = TestObserver<CheckoutViewState>()
        viewModel.viewState.observeForever(testObserver)

        /*When*/
        viewModel.load()

        /*Then*/
        Assert.assertFalse(testObserver.lastValue.isPayEnabled)
    }

    @Test
    fun `Given any cart When VM loads and is error Then error is dispatched`() {
        /*Given*/
        val viewModel = getViewModel(cartResponse = Left(GenericError))
        val testObserver = TestObserver<PresentationError>()
        viewModel.viewErrors.observeForever(testObserver)

        /*When*/
        viewModel.load()

        /*Then*/
        Assert.assertNotNull(testObserver.lastValue)
    }

    @Test
    fun `Given cart with no discounts When VM loads Then resume has no discounts`() {
        /*Given*/
        val cart = Right(MockData.buildMockCart(MockData.mockProducts))
        val viewModel = getViewModel(cartResponse = cart)
        val testObserver = TestObserver<CheckoutViewState>()
        viewModel.viewState.observeForever(testObserver)

        /*When*/
        viewModel.load()

        /*Then*/
        val resume = viewModel.getState().items.find { it is CheckoutResume } as CheckoutResume
        Assert.assertNull(resume.discount)
        Assert.assertNull(resume.discountBreakdown)
    }

    @Test
    fun `Given cart with discounts When VM loads Then resume has no discounts`() {
        /*Given*/
        val cart = Right(MockData.buildMockCart(cartProductsWithDiscount))
        val viewModel = getViewModel(cartResponse = cart)
        val testObserver = TestObserver<CheckoutViewState>()
        viewModel.viewState.observeForever(testObserver)

        /*When*/
        viewModel.load()

        /*Then*/
        val resume = viewModel.getState().items.find { it is CheckoutResume } as CheckoutResume
        Assert.assertNotNull(resume.discount)
        Assert.assertNotNull(resume.discountBreakdown)
    }

    @Test
    fun `Given cart with products When ContinueWithPayment is send and is success Then ShowPaymentConfirmation is dispatched`() {
        /*Given*/
        val viewModel = getViewModel()
        val testObserver = TestObserver<CheckoutViewAction>()
        viewModel.viewActions.observeForever(testObserver)

        /*When*/
        viewModel.load()
        viewModel.sendIntent(CheckoutIntent.ContinueWithPayment)

        /*Then*/
        Assert.assertTrue(testObserver.lastValue is CheckoutViewAction.ShowPaymentConfirmation)
    }

    @Test
    fun `Given cart with products When ContinueWithPayment is send and is error Then error is dispatched`() {
        /*Given*/
        val viewModel = getViewModel(payCartResponse = Left(GenericError))
        val testObserver = TestObserver<PresentationError>()
        viewModel.viewErrors.observeForever(testObserver)

        /*When*/
        viewModel.load()
        viewModel.sendIntent(CheckoutIntent.ContinueWithPayment)

        /*Then*/
        Assert.assertNotNull(testObserver.lastValue)
    }

    private fun getViewModel(
        cartResponse: Either<Cart> = Right(MockData.buildMockCart(MockData.mockProducts)),
        payCartResponse: Either<Unit> = Right(Unit)
    ): CheckoutViewModel {

        return CheckoutViewModel(
            getCartUseCase = mock {
                onBlocking { invoke() } doReturn flowOf(cartResponse)
            },
            payCartUseCase = mock {
                onBlocking { invoke() } doReturn payCartResponse
            },
            stringsProvider = stringsProvider,
            dispatchers = dispatchers
        )
    }
}
