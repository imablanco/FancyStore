@file:Suppress("TestFunctionName")

package com.ablanco.fancystore.features.products.ui

import com.ablanco.fancystore.base.BaseUiTest
import com.ablanco.fancystore.domain.base.Either
import com.ablanco.fancystore.domain.base.Right
import com.ablanco.fancystore.domain.models.Discount
import com.ablanco.fancystore.domain.models.Product
import com.ablanco.fancystore.features.products.presentation.ProductsViewModel
import com.ablanco.fancystore.mocks.MockData
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
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
    fun Given_empty_cart_When_loads_Then_cartBadgeIsGone() {
       
    }


}
